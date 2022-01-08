package core;

import java.util.LinkedList;

public class TradeHandler {
	public void uplink(String filename) {
		// Send the file
		LinkedList<String> peers = Core.pdh.getPeers();
		// Bail out if no peers
		if(peers.size()==0) {
			Core.logManager.log(this.getClass().getName(), "Not sufficient amount of peers! BAIL OUT");
			System.out.println("Not enough peers to upload");
			return;
		}
		
		
		
		
	}
	public void downlink(String metadataID) {
		// Download the file
		LinkedList<String> peers = Core.pdh.getPeers();
		// Bail out if no peers
		if(peers.size()==0) {
			Core.logManager.log(this.getClass().getName(), "Not sufficient amount of peers! BAIL OUT");
			System.out.println("Not enough peers to download");
			return;
		}
	}
}
