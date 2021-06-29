package datastructures;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

public class PeerDataHandler{
	HashMap<String, ArrayList<Object>> data;
	/**data has a String and ArrayList
	 * String is used to store ip address
	 * ArrayList is used to store information about the peer and how to communicate
	 * 
	 * Basically ArrayList is like this
	 * [id, inconnect, outputStream, replyQueue] 
	 * id -> Next packet id to send
	 * inconnect -> Is connection in or out
	 * outputStream -> To send requests into
	 * replyQueue -> A queue where reciever thread can put in requests
	 **/
	public PeerDataHandler() {
		this.create();
	}
	public void create() {
		data = new HashMap<String, ArrayList<Object>>();
	}
	public boolean addPeer(String peerIP,boolean inconnect, OutputStreamWriter outputStream, Queue<Object> replyQueue) {
		/** Adds a peer into the data handler, it will need
		 * peerIP which is ip of the peer as string 
		 * inconnect if connection is inbound, it needs to be true
		 * outputStream which is where we can write requests to
		 * replyQueue is where send request answers are spooled.**/
		if(!data.containsKey(peerIP)) {
			ArrayList<Object> al = new ArrayList<Object>();
			int id = 0;
			al.add(0, id);
			al.add(1, inconnect);
			al.add(2, outputStream);
			al.add(3, replyQueue);
			data.put(peerIP, al);
			return true;
		}
		else {
			return false;
		}
	}
	public boolean removePeer(String peerIP) {
		/** Returns true if removal was success else returns false **/
		if(data.containsKey(peerIP)) {
			data.remove(peerIP);
			return true;
		}
		else {
			return false;
		}
	}
	public int getid(String peerIP) {
		/** Returns id of the next packet given a valid peer non updating, please update manually, if not valid no idea about behavior **/
		ArrayList<Object> al = data.get(peerIP);
		int id = (int) al.get(0);
		return id;
	}
	public boolean getIn(String peerIP) {
		/** Returns in which is a boolean which says connection was inbound or outbound given a valid peer, if not valid no idea about behavior **/
		ArrayList<Object> al = data.get(peerIP);
		boolean inconn = (boolean) al.get(1);
		return inconn;
	}
	public OutputStreamWriter getOutputStream(String peerIP) {
		/** Returns OutputStreamWriter of a peer, basically it will give you a connection to write to peer given a valid peer, if not valid no idea about behavior **/
		ArrayList<Object> al = data.get(peerIP);
		OutputStreamWriter osw = (OutputStreamWriter) al.get(2);
		return osw;
	}
	public Queue<Object> getReplyQueue(String peerIP) {
		/** Returns Queue of the all the reply packets, if not valid no idea about behavior **/
		ArrayList<Object> al = data.get(peerIP);
		Queue<Object> q = (Queue<Object>) al.get(3);
		return q;
	}
	public void setid(String peerIP,int id) {
		/** Sets id which should be of next packet given a valid peer, if not valid no idea about behavior **/
		ArrayList<Object> al = data.get(peerIP);
		al.set(0, id);
	}
	public void setIn(String peerIP,boolean in) {
		/** Sets id which should be of next packet given a valid peer, if not valid no idea about behavior **/
		ArrayList<Object> al = data.get(peerIP);
		al.set(1, in);
	}
	public void setOutputStream(String peerIP,OutputStreamWriter osw) {
		/** Sets OutputStreamWriter which should be the new OutputStreamWriter of peer given a valid peer, if not valid no idea about behavior **/
		ArrayList<Object> al = data.get(peerIP);
		al.set(2, osw);
	}
	public void setReplyQueue(String peerIP,Queue<Object> q) {
		/** Sets reply Queue of a peer given a valid peer, if not valid no idea about behavior **/
		ArrayList<Object> al = data.get(peerIP);
		al.set(3, q);
	}
}