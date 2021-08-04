package connectivity;

import java.util.LinkedList;

import javax.sound.midi.SysexMessage;

import core.Core;

public class AutoConnector extends Thread{
	LinkedList<String> disconnectedPeers;
	public void peerListRefresh() {
		LinkedList<String> currentDisconnectedPeers = Core.pdh.getDisconnectedPeers();
		for(int i=0;i<currentDisconnectedPeers.size();i++) {
			String temp = currentDisconnectedPeers.get(i);
			if(!disconnectedPeers.contains(temp)) {
				disconnectedPeers.add(temp);
			}
		}
	}
	public String peerFinder() {
		String peerFound=null;
		if(disconnectedPeers.size()!=0) {
			peerFound = disconnectedPeers.pop();
			disconnectedPeers.add(peerFound);
		}
		return peerFound;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		disconnectedPeers = new LinkedList<String>();
		new TCPClient(Core.config.getBootStrapPeer());
		while(true){
			peerListRefresh();
			Core.logManager.log(this.getClass().getName(),"Disconnected Peers:"+disconnectedPeers.toString()); 
			String chosenPeer = peerFinder();
			Core.logManager.log(this.getClass().getName(),"Disconnected Peers:"+disconnectedPeers.toString()); 
			if(chosenPeer!=null) {
				if(Core.pdh.getConnected(chosenPeer)) {
					continue;
				}
				new TCPClient(chosenPeer);
			}
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public AutoConnector() {
		// TODO Auto-generated constructor stub
		
	}
}
