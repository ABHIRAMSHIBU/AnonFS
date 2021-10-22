package connectivity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import algorithm.ByteArrayTransforms;
import configuration.Configuration;
import core.Core;
import datastructures.CallBackPromise;
import datastructures.Packet;

public class TCPHander extends Thread {
	Socket clientSocket;
	long id;
	boolean server;
	public Packet p ;
	boolean redudantConnection = false;
	TCPHander(Socket clientSocket,long id2, boolean server) throws IOException {
		this.clientSocket=clientSocket;
		this.id = id2;
		this.server = server;
		p = new Packet();
		Queue<Object> q = new LinkedList<Object>();
		// TODO: Do something when the conflict comes.. May be prefer newest connection
		synchronized(Core.pdh) {
			if(! Core.pdh.entryExists(clientSocket.getInetAddress().getHostAddress())) {
				Core.pdh.addPeer(clientSocket.getInetAddress().getHostAddress(), server, new OutputStreamWriter(clientSocket.getOutputStream()), q, this, true);
				Core.logManager.log(this.getClass().getName(), "Address: "+clientSocket.getInetAddress().getHostAddress()+" added to Peer DB");
			}
			else {
				if(Core.pdh.getConnected(clientSocket.getInetAddress().getHostAddress())) {
					Core.logManager.critical(this.getClass().getName(), "Address: "+clientSocket.getInetAddress().getHostAddress()+" already exists in Peer DB! and connected! (May be redundant connection)");
					redudantConnection = true;
				}
				else {
					Core.pdh.setConnected(clientSocket.getInetAddress().getHostAddress(), true);
				}
			}
		}
	}
	/**Read line takes 1 argument
	 * InputStreamReader isr
	 * InputStreamReader isr is an object from which data will be read from
	 * @author abhiram
	 * @throws IOException 
	 * @throws InterruptedException 
	 * */
	public byte[] readLine(InputStreamReader isr) throws IOException, InterruptedException {
		byte z = 0x10;
		byte buffer[] = new byte[100];
		int pos=0;
		while(z!=0x0a) {
			if(clientSocket.isClosed()) {
				return null;
			}
			z=(byte) isr.read();
			if(z==-1) {
				if(pos==0) {
					return null;
				}
				byte[] retbuff = new byte[pos-1];
				for(int i=0;i<pos-1;i++) {
					retbuff[i] = buffer[i];
				}
				return retbuff;
			}
			buffer[pos]=z;
			pos++;
		}
		byte[] retbuff = new byte[pos-1];
		for(int i=0;i<pos-1;i++) {
			retbuff[i] = buffer[i];
		}
		return retbuff;
	}
	public byte[] readLine(InputStreamReader isr, long timeout) throws IOException, InterruptedException {
		//TODO Implement timeout
		byte z = 0x10;
		byte buffer[] = new byte[100];
		int pos=0;
		while(z!=0x0a) {
			if(clientSocket.isClosed()) {
				return null;
			}
			z=(byte) isr.read();
			if(z==-1) {
				if(pos==0) {
					return null;
				}
				byte[] retbuff = new byte[pos-1];
				for(int i=0;i<pos-1;i++) {
					retbuff[i] = buffer[i];
				}
				return retbuff;
			}
			buffer[pos]=z;
			pos++;
		}
		byte[] retbuff = new byte[pos-1];
		for(int i=0;i<pos-1;i++) {
			retbuff[i] = buffer[i];
		}
		return retbuff;
	}
	public void run() {
		if(redudantConnection) {
			Core.logManager.critical(this.getClass().getName(), "Address: "+clientSocket.getInetAddress().getHostAddress()+" is flagged redudent, TCPHander Quitting");
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		try { 
			    OutputStreamWriter out = new OutputStreamWriter(clientSocket.getOutputStream());
			    //BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			    InputStreamReader in = new InputStreamReader(clientSocket.getInputStream());
//			    if(server) {
//				    out.write("AnonFS "+Configuration.version+"\n");
//				    out.flush();
//			    }
			    while(true) {
			    	
			    	// Blocked read so no need for Thread.sleep
			    	HashMap<String, Object> packet = p.readInputStreamPacket(in);
			    	if(packet==null) {
			    		synchronized(Core.pdh) {
				    		//End of stream reached and socket is dead, so packup
				    		Core.logManager.log(this.getClass().getName(), "IP: " + clientSocket.getInetAddress().getHostAddress()  + " Client "+id+" disconnected!");
				    		//Core.pdh.removePeer(clientSocket.getInetAddress().getHostAddress());
				    		Core.pdh.setConnected(clientSocket.getInetAddress().getHostAddress(),false);
				    		Core.logManager.log(this.getClass().getName(), "Address: "+clientSocket.getInetAddress().getHostAddress()+" set connected to false");
				    		clientSocket.close();
				    		break;
			    		}
			    	}
			    	String message = new String((byte []) packet.get("body"));
//			    	byte line [] = readLine(in);
//			    	if(line==null) {
//			    		Core.logManager.log(this.getClass().getName(), "IP: " + clientSocket.getInetAddress().getHostAddress()  + " Client "+id+" disconnected!");
//			    		return;
//			    	}
//			    	String message = new String(line);
			    	
			    	//Enable the below line to log every message
			    	Core.logManager.log(this.getClass().getName(), "Got String:"+message+" refid:"+(byte)packet.get("refid"));
			    	// TODO: Change message to binary type.. otherwise might need base64 encode.
			    	try {
			    		HashMap<String, Object> packetWrapper = null;
			    		
			    		// At this point we know the inbound packet is a request not a reply
				    	if(p.getDecodedRequestFlag()==Packet.REQUEST) {
				    		Core.logManager.log(this.getClass().getName(),"request mode");
				    		
				    		// Command Info
					    	if(message.compareTo("info")==0) {
					    		Core.logManager.log(this.getClass().getName(), "ClientID:"+id+" asked info");
					    		packetWrapper = p.createPacket(ByteArrayTransforms.toByteArray("AnonFS "+Configuration.version+"\n"), Packet.REPLY, 1, (byte) packet.get("id"));
					    		out.write(ByteArrayTransforms.toCharArray((byte[]) packetWrapper.get("packet")));
					    		out.flush();
					    	}
					    	
					    	// Command GetPeerList
					    	else if(message.compareTo("GetPeerList")==0) {
					    		String peerString="";
					    		LinkedList<String> peerList = Core.pdh.getPeers();
					    		for(int i=0;i<peerList.size();i++) {
					    			peerString+=peerList.get(i);
					    			if(i+1<peerList.size()) {
					    				peerString+="\n";
					    			}
					    		}
					    		packetWrapper = p.createPacket(ByteArrayTransforms.toByteArray(peerString), Packet.REPLY, 1, (byte) packet.get("id"));
					    		out.write(ByteArrayTransforms.toCharArray((byte[]) packetWrapper.get("packet")));
					    		out.flush();
					    		//TODO Implement GetPeerList
					    	}
					    	
					    	// Command GetUID
					    	else if(message.compareTo("GetUID")==0) {
					    		packetWrapper = p.createPacket(ByteArrayTransforms.toByteArray(Core.UIDHander.getUIDString()), Packet.REPLY, 1, (byte) packet.get("id"));
					    		out.write(ByteArrayTransforms.toCharArray((byte[]) packetWrapper.get("packet")));
					    		out.flush();
					    	}
					    	
					    	// Command PushPiece
					    	else if(message.startsWith("PushPiece")) {
					    		packetWrapper = p.createPacket(ByteArrayTransforms.toByteArray("Not Implemented!"+"\n"), Packet.REPLY, 1, (byte) packet.get("id"));
					    		out.write(ByteArrayTransforms.toCharArray((byte[]) packetWrapper.get("packet")));
					    		out.flush();
					    		// TODO Implement PushPiece
					    	}
					    	
					    	// Command GetPiece
					    	else if(message.startsWith("GetPiece")) {
					    		packetWrapper = p.createPacket(ByteArrayTransforms.toByteArray("Not Implemented!"+"\n"), Packet.REPLY, 1, (byte) packet.get("id"));
					    		out.write(ByteArrayTransforms.toCharArray((byte[]) packetWrapper.get("packet")));
					    		out.flush();
					    		// TODO Implement GetPiece
					    	}
					    	
					    	// Command FindPiece
					    	else if(message.startsWith("FindPiece")) {
					    		packetWrapper = p.createPacket(ByteArrayTransforms.toByteArray("Not Implemented!"+"\n"), Packet.REPLY, 1, (byte) packet.get("id"));
					    		out.write(ByteArrayTransforms.toCharArray((byte[]) packetWrapper.get("packet")));
					    		out.flush();
					    		// TODO Implement FindPiece
					    	}
					    	
					    	// Command MyIP
					    	else if(message.startsWith("MyIP")) {
					    		packetWrapper = p.createPacket(ByteArrayTransforms.toByteArray(clientSocket.getInetAddress().getHostAddress()+"\n"), Packet.REPLY, 1, (byte) packet.get("id"));
					    		out.write(ByteArrayTransforms.toCharArray((byte[]) packetWrapper.get("packet")));
					    		out.flush();
					    	}
					    	
					    	// Command is not valid error condition
					    	else {
					    		packetWrapper = p.createPacket(ByteArrayTransforms.toByteArray("Invalid command!"+"\n"), Packet.REPLY, 1, (byte) packet.get("id"));
					    		out.write(ByteArrayTransforms.toCharArray((byte[]) packetWrapper.get("packet")));
					    		out.flush();
					    	}
				    	}
				    	
				    	// At this point we know that the inbound packet is a reply not a request.
				    	else {
				    		// Queue Packet in reply queue.
				    		// Check if there is a thing waiting reply, if yes, inject data
				    		Core.logManager.log(this.getClass().getName(),"reply mode");
				    		synchronized (Core.pdh) {
				    			Core.logManager.log(this.getClass().getName(),"got lock on pdh");
				    			Core.logManager.log(this.getClass().getName(),""+clientSocket.getInetAddress().getHostAddress().toString());
				    			Queue<Object> queue = Core.pdh.getReplyQueue(clientSocket.getInetAddress().getHostAddress().toString());	
					    		if(queue.size()>0) {
					    			int size = queue.size();
					    			for(int i=0;i<size;i++) { // Parse like a deck of cards
					    				CallBackPromise cbp = (CallBackPromise) queue.remove();
					    				if(cbp.refid!=(byte) packet.get("refid")) {
					    					queue.add(cbp);
					    					Core.logManager.log(this.getClass().getName(), "Message: "+message+" refid: "+(byte)packet.get("refid")+" does not match call back promise ref id: "+cbp.refid);
					    				}
					    				else {
					    					synchronized(cbp) {
					    						cbp.data = p.getDecodedData();
					    						Core.logManager.log(this.getClass().getName(), "Message: "+message+" refid: "+(byte)packet.get("refid")+" did match call back promise ref id: "+cbp.refid);
					    						cbp.notify();
					    						Core.logManager.log(this.getClass().getName(), "Notifying back");
					    					}
					    					break;
					    				}
					    			}
					    		}
				    		}
				    	}
			    	}
			    	catch(SocketException e) {
			    		//End of stream reached and socket is dead, so packup
			    		Core.logManager.log(this.getClass().getName(), "IP: " + clientSocket.getInetAddress().getHostAddress()  + " Client "+id+" disconnected!");
			    		Core.pdh.setConnected(clientSocket.getInetAddress().getHostAddress(),false);
			    		Core.logManager.log(this.getClass().getName(), "Address: "+clientSocket.getInetAddress().getHostAddress()+" removed from Peer DB");
			    		clientSocket.close();
			    		break;
			    	}
			    	// TODO write what to do here
			    }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static byte [] sendRequestGetData(String ip,String request) throws IOException, InterruptedException {
		synchronized (Core.pdh) {
			OutputStreamWriter osw = Core.pdh.getOutputStream(ip);
			HashMap<String, Object> packetWrapper = Core.pdh.getTCPHander(ip).p.createPacket(ByteArrayTransforms.toByteArray(request), Packet.REPLY, 1, (byte)0);
			byte [] packet = (byte[]) packetWrapper.get("packet");
			byte id = (byte) packetWrapper.get("id");
			CallBackPromise cbp = new CallBackPromise(id);
			Core.pdh.getReplyQueue(ip).add(cbp);
			synchronized (cbp) {
				osw.write(ByteArrayTransforms.toCharArray(packet));
				cbp.wait();
			}
			return cbp.data;
		}
	}
}