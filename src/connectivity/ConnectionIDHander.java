package connectivity;

public class ConnectionIDHander {
	static long id=0;
	synchronized long genID() {
		return id++;
	}
}
