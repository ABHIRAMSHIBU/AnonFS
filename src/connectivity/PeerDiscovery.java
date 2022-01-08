package connectivity;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import algorithm.ByteArrayTransforms;
import core.Core;
import datastructures.CallBackPromise;
import datastructures.Packet;
import datastructures.PeerDataHandler;

public class PeerDiscovery extends Thread {
	PeerDataHandler pdh = Core.pdh;
	public void run() {
		while(true) {
			LinkedList<String> peerList = null;
			synchronized (pdh) {
				peerList = pdh.getConnectedPeers();
			}
			int peerCount = peerList.size();
			for(int i=0;i<peerCount;i++) {
				String peer = peerList.get(i);
				Core.logManager.log(this.getClass().getName(), "Inside i="+i+" value = "+peer,4);
				TCPHander handler;
				Queue<Object> qcallback;
				OutputStreamWriter osw;
				int selfHost;
				
				synchronized (pdh) {
					handler = pdh.getTCPHander(peer);
					qcallback = pdh.getReplyQueue(peer);
					osw = pdh.getOutputStream(peer);
					selfHost = pdh.getSelfHost(peer);
				}
				HashMap<String, Object> packetWrapper = handler.p.createPacket(ByteArrayTransforms.toByteArray("GetPeerList"),Packet.REQUEST,0,(byte)0);
				byte packet[] = (byte[]) packetWrapper.get("packet");
				byte packetid = (byte) packetWrapper.get("id");
				
				// We dont know if its a self loop.
				CallBackPromise cbp = new CallBackPromise(packetid);
				if(selfHost == -1) {
					try {
						HashMap<String,Object> packetWrapper1 = handler.p.createPacket(ByteArrayTransforms.toByteArray("GetUID"),Packet.REQUEST,0,(byte)0);
						byte packet1[] = (byte[]) packetWrapper1.get("packet");
						byte packetid1 = (byte) packetWrapper1.get("id");
						CallBackPromise cbp1 = new CallBackPromise(packetid1);
						synchronized (cbp1) {
							qcallback.add(cbp1);
							if(handler.clientSocket.isClosed()) {
								Core.logManager.log(this.getClass().getName(), "Socket already closed for peer "+peer,4);
							}
							else {
								Core.logManager.log(this.getClass().getName(), "Socket is open proceeding for peer "+peer,4);
								osw.write(ByteArrayTransforms.toCharArray(packet1));
								osw.flush();
								cbp1.wait();
							}
							
						}

						byte recievedData1[] = cbp1.data;
						if(recievedData1!=null) {
							if(Core.UIDHander.getUIDString().equals(new String(recievedData1))) {
								// Self loop
								Core.logManager.log(this.getClass().getName(), "Loop detected "+peer,4);
								osw.close();
								handler.clientSocket.close();
								selfHost = 1;
							}
							else {
								Core.logManager.log(this.getClass().getName(), "No Loop detected "+peer,4);
								selfHost = 0;
							}
							pdh.setUID(peer, new String(recievedData1));
						}
						try {
							pdh.setSelfHost(peer, selfHost);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(selfHost == 1 || selfHost == -1) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				}
				
				synchronized (cbp) {
					try {
						qcallback.add(cbp);
						osw.write(ByteArrayTransforms.toCharArray(packet));
						osw.flush();
						Core.logManager.log(this.getClass().getName(), "Going to wait on Call Back Promise ID:"+handler.p.getCreatedID(),4);
						cbp.wait();
						Core.logManager.log(this.getClass().getName(), "Wait finished on Call Back Promise",4);
						
					} catch (SocketException e) {
						synchronized (pdh) {
							pdh.setConnected(peer, false);
						}
					}
					catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				byte recievedData[] = cbp.data;
				if(recievedData!=null) {
					String ipList[] = (new String(recievedData)).split("\n");
					LinkedList<String> peers =  Core.pdh.getPeers();
					for(int k=0;k<ipList.length;k++) {
						System.out.println(ipList[k]);
						if(peers.indexOf(ipList[k])==-1) {
							String [] potentialPeer = ipList[k].split("\t");
							new TCPClient(potentialPeer[0]);
							Core.logManager.log(this.getClass().getName(), "Connecting "+ipList[k],4);
							if(potentialPeer.length==2) {
								Core.pdh.setUID(potentialPeer[0],potentialPeer[1]);
							}
						}
						else {
							Core.logManager.log(this.getClass().getName(), "Already exists "+ipList[k],4);
						}
					}
					Core.logManager.log(this.getClass().getName(), new String(ByteArrayTransforms.toCharArray(recievedData)),4);
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
