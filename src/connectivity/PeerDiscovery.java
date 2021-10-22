package connectivity;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import algorithm.ByteArrayTransforms;
import core.Core;
import datastructures.CallBackPromise;
import datastructures.PeerDataHandler;
import logging.LogManager;

public class PeerDiscovery extends Thread {
	PeerDataHandler pdh = Core.pdh;
	public void run() {
		while(true) {
			LinkedList<String> peerList = pdh.getConnectedPeers();
			int peerCount = peerList.size();
			for(int i=0;i<peerCount;i++) {
				String peer = peerList.get(i);
				Core.logManager.log(this.getClass().getName(), "Inside i="+i+" value = "+peer);
				TCPHander handler = pdh.getTCPHander(peer);
				Queue<Object> qcallback = pdh.getReplyQueue(peer);
				OutputStreamWriter osw = pdh.getOutputStream(peer);
				HashMap<String, Object> packetWrapper = handler.p.createPacket(ByteArrayTransforms.toByteArray("GetPeerList"),"1".getBytes()[0],0,"0".getBytes()[0]);
				byte packetBody[] = (byte[]) packetWrapper.get("body");
				byte packetid = (byte) packetWrapper.get("id");
				CallBackPromise cbp = new CallBackPromise(packetid);
				synchronized (cbp) {
					try {
						qcallback.add(cbp);
						osw.write(ByteArrayTransforms.toCharArray(packetBody));
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
