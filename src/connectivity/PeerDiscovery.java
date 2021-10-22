package connectivity;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import algorithm.ByteArrayTransforms;
import core.Core;
import datastructures.CallBackPromise;
import datastructures.Packet;
import datastructures.PeerDataHandler;
import logging.LogManager;

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
				Core.logManager.log(this.getClass().getName(), "Inside i="+i+" value = "+peer);
				TCPHander handler;
				Queue<Object> qcallback;
				OutputStreamWriter osw;
				synchronized (pdh) {
					handler = pdh.getTCPHander(peer);
					qcallback = pdh.getReplyQueue(peer);
					osw = pdh.getOutputStream(peer);
				}
				HashMap<String, Object> packetWrapper = handler.p.createPacket(ByteArrayTransforms.toByteArray("GetPeerList"),Packet.REQUEST,0,(byte)0);
				byte packet[] = (byte[]) packetWrapper.get("packet");
				byte packetid = (byte) packetWrapper.get("id");
				
				
				HashMap<String, Object> testpacket = handler.p.decodePacket(packet);
				Core.logManager.log(this.getClass().getName(), "Refid:"+(byte)testpacket.get("refid")+" id:"+(byte)testpacket.get("id")+" idpacketWrap:"+packetid);
				
				
				
				
				CallBackPromise cbp = new CallBackPromise(packetid);
				synchronized (cbp) {
					try {
						qcallback.add(cbp);
						osw.write(ByteArrayTransforms.toCharArray(packet));
						osw.flush();
						Core.logManager.log(this.getClass().getName(), "Going to wait on Call Back Promise ID:"+handler.p.getCreatedID());
						cbp.wait();
						Core.logManager.log(this.getClass().getName(), "Wait finished on Call Back Promise");
						
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				byte recievedData[] = cbp.data;
				Core.logManager.log(this.getClass().getName(), new String(ByteArrayTransforms.toCharArray(recievedData)));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	private CallBackPromise CallBackPromise(byte packetid) {
		// TODO Auto-generated method stub
		return null;
	}
}
