package core;

import java.util.UUID;

public class PeerUIDHander {
	private UUID UID;
	public void resetUID() {
		UID = UUID.randomUUID();
	}
	public PeerUIDHander(){
		resetUID();
	}
	public String getUIDString() {
		return UID.toString();
	}
	public UUID getUID() {
		return UID;
	}
}
