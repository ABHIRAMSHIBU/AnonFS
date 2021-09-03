package connectivity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
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
			    	int count=0;
			    	byte [] packet = p.readInputStreamPacket(in);
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
			    	String message = new String(p.getDecodedData());
//			    	byte line [] = readLine(in);
//			    	if(line==null) {
//			    		Core.logManager.log(this.getClass().getName(), "IP: " + clientSocket.getInetAddress().getHostAddress()  + " Client "+id+" disconnected!");
//			    		return;
//			    	}
//			    	String message = new String(line);
			    	
			    	//Enable the below line to log every message
			    	Core.logManager.log(this.getClass().getName(), "Got String:"+message);
			    	try {
				    	if(p.getDecodedRequestFlag()==Packet.REQUEST) {
					    	if(message.compareTo("info")==0) {
					    		Core.logManager.log(this.getClass().getName(), "ClientID:"+id+" asked info");
					    		out.write(ByteArrayTransforms.toCharArray(p.createPacket(ByteArrayTransforms.toByteArray("AnonFS "+Configuration.version+"\n"), Packet.REPLY, 1, p.getDecodedrefid())));
					    		out.flush();
					    	}
					    	else if(message.compareTo("GetPeerList")==0) {
					    		String peerString="";
					    		LinkedList<String> peerList = Core.pdh.getPeers();
					    		for(int i=0;i<peerList.size();i++) {
					    			peerString+=peerList.get(i);
					    			if(i+1<peerList.size()) {
					    				peerString+="\n";
					    			}
					    		}
					    		out.write(ByteArrayTransforms.toCharArray(p.createPacket(ByteArrayTransforms.toByteArray(peerString), Packet.REPLY, 1, p.getDecodedrefid())));
					    		out.flush();
					    		//TODO Implement GetPeerList
					    	}
					    	else if(message.compareTo("GetUID")==0) {
					    		out.write(ByteArrayTransforms.toCharArray(p.createPacket(ByteArrayTransforms.toByteArray(Core.UIDHander.getUIDString()), Packet.REPLY, 1, p.getDecodedrefid())));
					    		out.flush();
					    	}
					    	else if(message.startsWith("PushPiece")) {
					    		out.write(ByteArrayTransforms.toCharArray(p.createPacket(ByteArrayTransforms.toByteArray("Not Implemented!"+"\n"), Packet.REPLY, 1, p.getDecodedrefid())));
					    		out.flush();
					    		// TODO Implement PushPiece
					    	}
					    	else if(message.startsWith("GetPiece")) {
					    		out.write(ByteArrayTransforms.toCharArray(p.createPacket(ByteArrayTransforms.toByteArray("Not Implemented!"+"\n"), Packet.REPLY, 1, p.getDecodedrefid())));
					    		out.flush();
					    		// TODO Implement GetPiece
					    	}
					    	else if(message.startsWith("FindPiece")) {
					    		out.write(ByteArrayTransforms.toCharArray(p.createPacket(ByteArrayTransforms.toByteArray("Not Implemented!"+"\n"), Packet.REPLY, 1, p.getDecodedrefid())));
					    		out.flush();
					    		// TODO Implement FindPiece
					    	}
					    	else if(message.startsWith("MyIP")) {
					    		out.write(ByteArrayTransforms.toCharArray(p.createPacket(ByteArrayTransforms.toByteArray(clientSocket.getInetAddress().getHostAddress()+"\n"), Packet.REPLY, 1, p.getDecodedrefid())));
					    		out.flush();
					    	}
					    	else {
				    			out.write(ByteArrayTransforms.toCharArray(p.createPacket(ByteArrayTransforms.toByteArray("Invalid command!"+"\n"), Packet.REPLY, 1, p.getDecodedrefid())));
					    		out.flush();
					    	}
				    	}
				    	else {
				    		// Queue Packet in reply queue.
				    		// Check if there is a thing waiting reply, if yes, inject data
				    		Queue<Object> queue = Core.pdh.getReplyQueue(clientSocket.getInetAddress().getAddress().toString());
				    		if(queue.size()>0) {
				    			int size = queue.size();
				    			for(int i=0;i<size;i++) { // Parse like a deck of cards
				    				CallBackPromise cbp = (CallBackPromise) queue.remove();
				    				if(cbp.refid!=p.getDecodedrefid()) {
				    					queue.add(cbp);
				    				}
				    				else {
				    					synchronized(cbp) {
				    						cbp.data = p.getDecodedData();
				    						cbp.notify();
				    					}
				    					break;
				    					
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
		OutputStreamWriter osw = Core.pdh.getOutputStream(ip);
		byte [] packet = Core.pdh.getTCPHander(ip).p.createPacket(ByteArrayTransforms.toByteArray(request), Packet.REPLY, 1, (byte)0);
		byte id = Core.pdh.getTCPHander(ip).p.getCreatedID();
		CallBackPromise cbp = new CallBackPromise(id);
		Core.pdh.getReplyQueue(ip).add(cbp);
		synchronized (cbp) {
			osw.write(ByteArrayTransforms.toCharArray(packet));
			cbp.wait();
		}
		return cbp.data;
	}
}