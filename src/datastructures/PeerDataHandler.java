package datastructures;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import connectivity.TCPHander;
import core.Core;

public class PeerDataHandler{
	HashMap<String, ArrayList<Object>> data;
	public boolean failure = true;
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
		failure = false;
	}
	
	public void create() {
		data = new HashMap<String, ArrayList<Object>>();
	}
	
	/**
	 * Adds a peer into the data handler, it will need
	 * peerIP which is ip of the peer as string 
	 * inconnect if connection is inbound, it needs to be true
	 * outputStream which is where we can write requests to
	 * replyQueue is where send request answers are spooled.
	 * tcpHandler is the hander which handles connection of
	 * the peer.
	 * @param peerIP
	 * @param inconnect
	 * @param outputStream
	 * @param replyQueue
	 * @param tcpHandler
	 * @return
	 */
	public boolean addPeer(String peerIP,boolean inconnect, OutputStreamWriter outputStream, Queue<Object> replyQueue, TCPHander tcpHandler, boolean connected) {
		if(!data.containsKey(peerIP)) {
			Core.logManager.log(getClass().getName(), "Adding peer to list with IP:"+peerIP,4);
			ArrayList<Object> al = new ArrayList<Object>();
			int id = 0;
			int selfhost = -1;
			al.add(0, id);
			al.add(1, inconnect);
			al.add(2, outputStream);
			al.add(3, replyQueue);
			al.add(4, tcpHandler);
			al.add(5, connected);
			al.add(6, selfhost);
			data.put(peerIP, al);
			return true;
		}
		else {
			Core.logManager.log(getClass().getName(), "Adding peer failed, exists IP:"+peerIP,4);
			return false;
		}
		//TODO:  Remove unused stuff from Peer Data Handler
	}
	/** 
	 * Returns true if removal was success else returns false
	 * @param peerIP
	 * @return
	 */
	public boolean removePeer(String peerIP) {
		
		if(data.containsKey(peerIP)) {
			data.remove(peerIP);
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * Returns id of the next packet given a valid peer non updating, please update manually, if not valid no idea about behavior
	 * @param peerIP
	 * @return
	 */
	public int getid(String peerIP) {
		ArrayList<Object> al = data.get(peerIP);
		int id = (int) al.get(0);
		return id;
	}
	/**
	 * Returns TCPHander that handles the IP, if not valid no idea about behavior
	 * @param peerIP
	 * @return
	 */
	public TCPHander getTCPHander(String peerIP) {
		ArrayList<Object> al = data.get(peerIP);
		TCPHander tcpHandler = (TCPHander) al.get(4);
		return tcpHandler;
	}
	/**
	 * Returns in which is a boolean which says connection was inbound or outbound given a valid peer, if not valid no idea about behavior
	 * @param peerIP
	 * @return
	 */
	public boolean getIn(String peerIP) {
		ArrayList<Object> al = data.get(peerIP);
		boolean inconn = (boolean) al.get(1);
		return inconn;
	}
	
	/**
	 * Returns in which is a boolean which says connection was inbound or outbound given a valid peer, if not valid no idea about behavior
	 * @param peerIP
	 * @return
	 */
	public boolean getConnected(String peerIP) {
		ArrayList<Object> al = data.get(peerIP);
		boolean inconn = (boolean) al.get(5);
		return inconn;
	}
	
	/**
	 * Returns OutputStreamWriter of a peer, basically it will give you a connection to write to peer given a valid peer, if not valid no idea about behavior
	 * @param peerIP
	 * @return
	 */
	public OutputStreamWriter getOutputStream(String peerIP) {
		ArrayList<Object> al = data.get(peerIP);
		OutputStreamWriter osw = (OutputStreamWriter) al.get(2);
		return osw;
	}
	
	/**
	 * Returns Queue of the all the reply packets, if not valid no idea about behavior
	 * @param peerIP
	 * @return
	 */
	public Queue<Object> getReplyQueue(String peerIP) {
		ArrayList<Object> al = data.get(peerIP);
		Queue<Object> q = (Queue<Object>) al.get(3);
		return q;
	}
	
	/**
	 * Returns if the host is a selfloop or not. Selfloop applies to hosts with multiple IP address.
	 * @param peerIP
	 * @return Integer with value -1,0,1 -> Not Sure, Not Loop, Is Loop
	 */
	public int getSelfHost(String peerIP) {
		/** Sets reply Queue of a peer given a valid peer, if not valid no idea about behavior **/
		ArrayList<Object> al = data.get(peerIP);
		return (int) al.get(6);
	}
	
	/**
	 * Sets id which should be of next packet given a valid peer, if not valid no idea about behavior
	 * @param peerIP
	 * @param id
	 */
	public void setid(String peerIP,int id) {
		ArrayList<Object> al = data.get(peerIP);
		al.set(0, id);
	}
	
	/**
	 * Sets id which should be of next packet given a valid peer, if not valid no idea about behavior
	 * @param peerIP
	 * @param in
	 */
	public void setIn(String peerIP,boolean in) {
		ArrayList<Object> al = data.get(peerIP);
		al.set(1, in);
	}
	
	/**
	 * Sets OutputStreamWriter which should be the new OutputStreamWriter of peer given a valid peer, if not valid no idea about behavior
	 * @param peerIP
	 * @param osw
	 */
	public void setOutputStream(String peerIP,OutputStreamWriter osw) {
		ArrayList<Object> al = data.get(peerIP);
		al.set(2, osw);
	}
	
	/**
	 * Sets reply Queue of a peer given a valid peer, if not valid no idea about behavior
	 * @param peerIP
	 * @param q
	 */
	public void setReplyQueue(String peerIP,Queue<Object> q) {
		/** Sets reply Queue of a peer given a valid peer, if not valid no idea about behavior **/
		ArrayList<Object> al = data.get(peerIP);
		al.set(3, q);
	}
	
	/**
	 * Sets connected status of a peer given a valid peer, if not valid no idea about behavior
	 * @param peerIP
	 * @param connected
	 */
	public void setConnected(String peerIP,boolean connected) {
		/** Sets reply Queue of a peer given a valid peer, if not valid no idea about behavior **/
		ArrayList<Object> al = data.get(peerIP);
		al.set(5, connected);
	}
	
	/**
	 * Sets if a ip address is a loop or not.
	 * @param peerIP - IP address of the peer to manipulate
	 * @param SelfHost - -1,0,1 -> Not sure, not loop, is loop
	 * @throws Exception 
	 */
	public void setSelfHost(String peerIP,int SelfHost) throws Exception {
		/** Sets reply Queue of a peer given a valid peer, if not valid no idea about behavior **/
		ArrayList<Object> al = data.get(peerIP);
		if(SelfHost>=-1 && SelfHost<=1) {
			al.set(6, SelfHost);
		}
		else {
			throw new Exception("Invalid Argument Value "+SelfHost+" !");
		}
	}
	
	/**
	 * Returns true if peer already exists
	 * @param peerIP
	 * @return
	 */
	public boolean entryExists(String peerIP) {
		if(data.containsKey(peerIP)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Returns the list of peers in the PeerDataHander
	 * @return
	 */
	public LinkedList<String> getPeers() {
		Set<String> keys = data.keySet();
		LinkedList<String> keys_list = new LinkedList<String>(keys);
		//String [] keys_array = (String[]) keys.toArray();
		return keys_list;
	}
	
	/**
	 * Returns the list of peers which are connected
	 * @return
	 */
	public LinkedList<String> getConnectedPeers(){
		Set<String> keys = data.keySet();
		LinkedList<String> connected_list = new LinkedList<String>();
		if(keys.size()>0) {
			LinkedList<String> keys_list = new LinkedList<String>(keys);
			for(int i=0;i<keys_list.size();i++) {
				String current = keys_list.get(i);
				Core.logManager.log(getClass().getName(), "ConnectedPeers:Value of current:"+current+" keys_list.size():"+keys_list.size(),4);
				boolean connected = getConnected(current);
				if(connected==true) {
					Core.logManager.log(getClass().getName(), "ConnectedPeers: "+current+" is connected",4);
					connected_list.add(keys_list.get(i));
				}
			}
		}
		return connected_list;
	}
	
	/**
	 * Returns the count of connected peers
	 * @return
	 */
	public int getConnectedPeersCount() {
		return getConnectedPeers().size();
	}
	
	/**
	 * Returns the list of peers which are disconnected
	 * @return
	 */
	public LinkedList<String> getDisconnectedPeers(){
		Set<String> keys = data.keySet();
		LinkedList<String> keys_list = new LinkedList<String>(keys);
		LinkedList<String> disconnected_list = new LinkedList<String>();
		for(int i=0;i<keys_list.size();i++) {
			String current = keys_list.get(i);
			Core.logManager.log(getClass().getName(), "DisconnectedPeers: Value of current:"+current+" keys_list.size():"+keys_list.size(),4);
			boolean connected = getConnected(current);
			if(connected==false) {
				disconnected_list.add(keys_list.get(i));
			}
		}
		return disconnected_list;
	}
	
	/**
	 * Returns the count of disconnected peers
	 * @return
	 */
	public int getDisconnectedPeersCount() {
		return getDisconnectedPeers().size();
	}
}