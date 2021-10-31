package connectivity;

import java.util.LinkedList;

import javax.sound.midi.SysexMessage;

import core.Core;

public class AutoConnector extends Thread{
	LinkedList<String> disconnectedPeers;
	public void peerListRefresh() {
		LinkedList<String> currentDisconnectedPeers=null;
		int tries = 0;
		while(true) {
			synchronized (Core.pdh) {
				try {
					currentDisconnectedPeers = Core.pdh.getDisconnectedPeers();
					break;
				}
				catch (NullPointerException e){
					Core.logManager.log(getClass().getName(), "Unable to lockonto the peers try-"+tries,4);
					tries+=1;
					if(tries==10) {
						Core.logManager.critical(getClass().getName(), "Tries over exiting loop anyway");
						e.printStackTrace();
						Thread.dumpStack();
						break;
					}
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i=0;i<currentDisconnectedPeers.size();i++) {
			String temp = currentDisconnectedPeers.get(i);
			if(!disconnectedPeers.contains(temp)) {
				disconnectedPeers.add(temp);
			}
		}
	}
	/** Find disconnected peer and return it
	 * 
	 * @return
	 */
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
			String chosenPeer = peerFinder();
			Core.logManager.log(this.getClass().getName(),"Disconnected Peers:"+disconnectedPeers.toString(),4); 
			if(chosenPeer!=null) {
				/* Bug fix
				 * Remove chosen peer if already connected, otherwise will lead to indefinite loop
				 */
				if(Core.pdh.getConnected(chosenPeer)) {
					disconnectedPeers.remove(chosenPeer);
					Core.logManager.log(this.getClass().getName(), "Removed disconnected peer "+chosenPeer,4);
					continue;
				}
				synchronized (Core.pdh) {
					int selfHost = Core.pdh.getSelfHost(chosenPeer);
					if(selfHost == -1 || selfHost == 1) {
						// Its either a loop or not determined so dont connect for now.
						if(selfHost == -1)
							Core.logManager.log(this.getClass().getName(),"Peer "+chosenPeer+" is not confirmed",4); 
						else
							Core.logManager.log(this.getClass().getName(),"Peer "+chosenPeer+" self loop",4); 
					}
					else {
						new TCPClient(chosenPeer);
						Core.logManager.log(this.getClass().getName(),"Peer "+chosenPeer+" TCPClient initiated",4);
					}
				}
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
