package connectivity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.ClientInfoStatus;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import algorithm.ByteArrayTransforms;
import configuration.Configuration;
import core.Core;
import datastructures.CallBackPromise;
import datastructures.Packet;
import datastructures.Piece;

public class TCPHander extends Thread {
	SocketChannel clientSocket;
	long id;
	boolean server;
	public Packet p ;
	boolean redudantConnection = false;
//	OutputStreamWriter out;
//	InputStreamReader in;
	TCPHander(SocketChannel clientSocket,long id2, boolean server) throws IOException {
		this.clientSocket=clientSocket;
		this.id = id2;
		this.server = server;
		p = new Packet();
		Queue<Object> q = new LinkedList<Object>();
		// TODO: Do something when the conflict comes.. May be prefer newest connection
		
		
		synchronized(Core.pdh) {
			if(! Core.pdh.entryExists(clientSocket.getRemoteAddress().toString().split(":")[0].substring(1))) {
//				out = new OutputStreamWriter(clientSocket.getOutputStream());
				Core.pdh.addPeer(clientSocket.getRemoteAddress().toString().split(":")[0].substring(1), server, clientSocket, q, this, true);
				Core.logManager.log(this.getClass().getName(), "Address: "+clientSocket.getRemoteAddress().toString().split(":")[0].substring(1)+" added to Peer DB",3);
			}
			else {
				if(Core.pdh.getConnected(clientSocket.getRemoteAddress().toString().split(":")[0].substring(1))) {
					Core.logManager.critical(this.getClass().getName(), "Address: "+clientSocket.getRemoteAddress().toString().split(":")[0].substring(1)+" already exists in Peer DB! and connected! (May be redundant connection)");
					redudantConnection = true;
				}
				else {
					Core.pdh.setConnected(clientSocket.getRemoteAddress().toString().split(":")[0].substring(1), true);
				}
			}
		}
	}
	public void run() {
		try {
			this.setName("TCPHandler for "+clientSocket.getRemoteAddress().toString().split(":")[0]);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if(redudantConnection) {
			try {
				Core.logManager.critical(this.getClass().getName(), "Address: "+clientSocket.getRemoteAddress().toString().split(":")[0].substring(1)+" is flagged redundant, TCPHander Quitting");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		try { 
			    //BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//			    if(server) {
//				    out.write("AnonFS "+Configuration.version+"\n");
//				    out.flush();
//			    }
			    while(true) {
			    	
			    	// Blocked read so no need for Thread.sleep
			    	HashMap<String, Object> packet = p.readInputStreamPacket(clientSocket);
			    	if(packet==null) {
			    		synchronized(Core.pdh) {
				    		//End of stream reached and socket is dead, so packup
				    		Core.logManager.log(this.getClass().getName(), "IP: " + clientSocket.getRemoteAddress().toString().split(":")[0].substring(1)  + " Client "+id+" disconnected!",3);
				    		//Core.pdh.removePeer(clientSocket.getInetAddress().getHostAddress());
				    		Core.pdh.setConnected(clientSocket.getRemoteAddress().toString().split(":")[0].substring(1),false);
				    		Core.logManager.log(this.getClass().getName(), "Address: "+clientSocket.getRemoteAddress().toString().split(":")[0].substring(1)+" set connected to false",4);
				    		clientSocket.close();
				    		break;
			    		}
			    	}
			    	String message = new String((byte []) packet.get("body"));
			    	Core.logManager.log(this.getClass().getName(), "Got message:"+message,4);
//			    	byte line [] = readLine(in);
//			    	if(line==null) {
//			    		Core.logManager.log(this.getClass().getName(), "IP: " + clientSocket.getInetAddress().getHostAddress()  + " Client "+id+" disconnected!");
//			    		return;
//			    	}
//			    	String message = new String(line);
			    	
			    	//Enable the below line to log every message
			    	Core.logManager.log(this.getClass().getName(), "Got String:"+message+" refid:"+(byte)packet.get("refid"),4);
			    	// TODO: Change message to binary type.. otherwise might need base64 encode.
			    	try {
			    		HashMap<String, Object> packetWrapper = null;
			    		
			    		// At this point we know the inbound packet is a request not a reply
				    	if(p.getDecodedRequestFlag()==Packet.REQUEST) {
				    		Core.logManager.log(this.getClass().getName(),"request mode");
				    		
				    		// Command Info
					    	if(message.compareTo("info")==0) {
					    		Core.logManager.log(this.getClass().getName(), "ClientID:"+id+" asked info",4);
					    		packetWrapper = p.createPacket(ByteArrayTransforms.toByteArray("AnonFS "+Configuration.version+"\n"), Packet.REPLY, 1, (byte) packet.get("id"));
					    		byte [] data = (byte[]) packetWrapper.get("packet");
				    			ByteBuffer buffer = ByteBuffer.allocate(data.length);
				    			buffer.put(data);
					    		synchronized (clientSocket) {
					    			while(buffer.hasRemaining()) {
					    				clientSocket.write(buffer);
					    			}
								}
					    	}
					    	
					    	// Command GetPeerList
					    	else if(message.compareTo("GetPeerList")==0) {
					    		String peerString="";
					    		LinkedList<String> peerList = Core.pdh.getPeers();
					    		for(int i=0;i<peerList.size();i++) {
					    			if(! clientSocket.getRemoteAddress().toString().split(":")[0].substring(1).equals(peerList.get(i))) {
						    			peerString+=peerList.get(i);
						    			if(i+1<peerList.size()) {
						    				peerString+="\n";
						    			}
					    			}
					    		}
					    		packetWrapper = p.createPacket(ByteArrayTransforms.toByteArray(peerString), Packet.REPLY, 1, (byte) packet.get("id"));
					    		byte [] data = (byte[]) packetWrapper.get("packet");
				    			ByteBuffer buffer = ByteBuffer.allocate(data.length);
				    			buffer.put(data);
				    			buffer.flip();
					    		synchronized (clientSocket) {
					    			while(buffer.hasRemaining()) {
					    				clientSocket.write(buffer);
					    			}
								}
					    		//TODO Implement GetPeerList
					    	}
					    	
					    	// Command GetUID
					    	else if(message.compareTo("GetUID")==0) {
					    		packetWrapper = p.createPacket(ByteArrayTransforms.toByteArray(Core.UIDHander.getUIDString()), Packet.REPLY, 1, (byte) packet.get("id"));
					    		byte [] data = (byte[]) packetWrapper.get("packet");
				    			ByteBuffer buffer = ByteBuffer.allocate(data.length);
				    			buffer.put(data);
				    			buffer.flip();
					    		synchronized (clientSocket) {
					    			while(buffer.hasRemaining()) {
					    				clientSocket.write(buffer);
					    			}
								}
					    	}
					    	
					    	// Command PushPiece
					    	// TODO: Implement handler for Message empty or invalid.
					    	else if(message.startsWith("PushPiece")) {
					    		message = message.substring(9);
					    		Core.logManager.log(this.getClass().getName(), "Command PushPiece Data:"+message);
					    		Piece piece = new Piece();
					    		if(!message.equals("")) {
						    		piece.fromString(message);
						    		if(Core.pieceDiskStorage.pieceToDisk(piece)) {	
						    			// Write success
						    			packetWrapper = p.createPacket(ByteArrayTransforms.toByteArray("ACK"+"\n"), Packet.REPLY, 1, (byte) packet.get("id"));
						    		}
						    		else {
						    			// Write Fail
						    			packetWrapper = p.createPacket(ByteArrayTransforms.toByteArray("NACK"+"\n"), Packet.REPLY, 1, (byte) packet.get("id"));
						    		}
						    		byte [] data = (byte[]) packetWrapper.get("packet");
					    			ByteBuffer buffer = ByteBuffer.allocate(data.length);
					    			buffer.put(data);
					    			buffer.flip();
						    		synchronized (clientSocket) {
						    			while(buffer.hasRemaining()) {
						    				clientSocket.write(buffer);
						    			}
									}
						    		// TODO Implement PushPiece
					    		}
					    	}
					    	
					    	// Command GetPiece
					    	// TODO: Implement handler for Message empty or invalid.
					    	else if(message.startsWith("GetPiece")) {
					    		message = message.substring(8);
					    		Core.logManager.log(this.getClass().getName(), "Command GetPiece Data:"+message);
					    		Piece piece = Core.pieceDiskStorage.diskToPiece(message);
					    		if(piece==null) {
					    			// PDNE -> Piece Do Not Exist
					    			packetWrapper = p.createPacket(ByteArrayTransforms.toByteArray("PDNE!"+"\n"), Packet.REPLY, 1, (byte) packet.get("id"));
					    		}
					    		else {
					    			packetWrapper = p.createPacket(ByteArrayTransforms.toByteArray(piece.toString()+"\n"), Packet.REPLY, 1, (byte) packet.get("id"));
					    		}
					    		byte [] data = (byte[]) packetWrapper.get("packet");
				    			ByteBuffer buffer = ByteBuffer.allocate(data.length);
				    			buffer.put(data);
				    			buffer.flip(); // Flip is very important.
					    		
				    			synchronized (clientSocket) {
					    			while(buffer.hasRemaining()) {
					    				clientSocket.write(buffer);
					    			}
								}
					    		// TODO Implement GetPiece
					    	}
					    	
					    	// Command FindPiece
					    	else if(message.startsWith("FindPiece")) {
					    		packetWrapper = p.createPacket(ByteArrayTransforms.toByteArray("Not Implemented!"+"\n"), Packet.REPLY, 1, (byte) packet.get("id"));
					    		byte [] data = (byte[]) packetWrapper.get("packet");
				    			ByteBuffer buffer = ByteBuffer.allocate(data.length);
				    			buffer.put(data);
					    		synchronized (clientSocket) {
					    			while(buffer.hasRemaining()) {
					    				clientSocket.write(buffer);
					    			}
								}
					    		// TODO Implement FindPiece
					    	}
					    	
					    	// Command MyIP
					    	else if(message.startsWith("MyIP")) {
					    		packetWrapper = p.createPacket(ByteArrayTransforms.toByteArray(clientSocket.getRemoteAddress().toString().split(":")[0].substring(1)+"\n"), Packet.REPLY, 1, (byte) packet.get("id"));
					    		byte [] data = (byte[]) packetWrapper.get("packet");
				    			ByteBuffer buffer = ByteBuffer.allocate(data.length);
				    			buffer.put(data);
					    		synchronized (clientSocket) {
					    			while(buffer.hasRemaining()) {
					    				clientSocket.write(buffer);
					    			}
								}
					    	}
					    	
					    	// Command is not valid error condition
					    	else {
					    		packetWrapper = p.createPacket(ByteArrayTransforms.toByteArray("Invalid command!"+"\n"), Packet.REPLY, 1, (byte) packet.get("id"));
					    		byte [] data = (byte[]) packetWrapper.get("packet");
				    			ByteBuffer buffer = ByteBuffer.allocate(data.length);
				    			buffer.put(data);
					    		synchronized (clientSocket) {
					    			while(buffer.hasRemaining()) {
					    				clientSocket.write(buffer);
					    			}
								}
					    	}
				    	}
				    	
				    	// At this point we know that the inbound packet is a reply not a request.
				    	else {
				    		// Queue Packet in reply queue.
				    		// Check if there is a thing waiting reply, if yes, inject data
				    		Core.logManager.log(this.getClass().getName(),"reply mode",4);
				    		synchronized (Core.pdh) {
				    			Core.logManager.log(this.getClass().getName(),"got lock on pdh",4);
				    			Core.logManager.log(this.getClass().getName(),""+clientSocket.getRemoteAddress().toString().split(":")[0].substring(1),4);
				    			Queue<Object> queue = Core.pdh.getReplyQueue(clientSocket.getRemoteAddress().toString().split(":")[0].substring(1));	
					    		if(queue.size()>0) {
					    			int size = queue.size();
					    			for(int i=0;i<size;i++) { // Parse like a deck of cards
					    				CallBackPromise cbp = (CallBackPromise) queue.remove();
					    				if(cbp.refid!=(byte) packet.get("refid")) {
					    					queue.add(cbp);
					    					Core.logManager.log(this.getClass().getName(), "Message: "+message+" refid: "+(byte)packet.get("refid")+" does not match call back promise ref id: "+cbp.refid,4);
					    				}
					    				else {
					    					synchronized(cbp) {
					    						cbp.data = p.getDecodedData();
					    						Core.logManager.log(this.getClass().getName(), "Message: "+message+" refid: "+(byte)packet.get("refid")+" did match call back promise ref id: "+cbp.refid,4);
					    						cbp.notify();
					    						Core.logManager.log(this.getClass().getName(), "Notifying back",4);
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
			    		Core.logManager.log(this.getClass().getName(), "IP: " + clientSocket.getRemoteAddress().toString().split(":")[0].substring(1)  + " Client "+id+" disconnected!",4);
			    		Core.pdh.setConnected(clientSocket.getRemoteAddress().toString().split(":")[0].substring(1),false);
			    		Core.logManager.log(this.getClass().getName(), "Address: "+clientSocket.getRemoteAddress().toString().split(":")[0].substring(1)+" removed from Peer DB",4);
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
	// TODO: Probably dead code.. if yes please remove
	public static byte [] sendRequestGetData(String ip,String request) throws IOException, InterruptedException {
		synchronized (Core.pdh) {
			SocketChannel socketChannel = Core.pdh.getOutputStream(ip);
			HashMap<String, Object> packetWrapper = Core.pdh.getTCPHander(ip).p.createPacket(ByteArrayTransforms.toByteArray(request), Packet.REPLY, 1, (byte)0);
			byte [] packet = (byte[]) packetWrapper.get("packet");
			byte id = (byte) packetWrapper.get("id");
			CallBackPromise cbp = new CallBackPromise(id);
			Core.pdh.getReplyQueue(ip).add(cbp);
			ByteBuffer buffer = ByteBuffer.allocate(packet.length);
			buffer.put(packet);
			synchronized (cbp) {
				synchronized (socketChannel) {
					while(buffer.hasRemaining()) {
						socketChannel.write(buffer);
					}
				}
				cbp.wait();
			}
			return cbp.data;
		}
	}
}