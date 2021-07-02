package connectivity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import configuration.Configuration;
import core.Core;

public class TCPHander extends Thread {
	Socket clientSocket;
	int id;
	boolean server;
	TCPHander(Socket clientSocket,int id, boolean server) {
		this.clientSocket=clientSocket;
		this.id = id;
		this.server = server;
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
		try { 
			    OutputStreamWriter out = new OutputStreamWriter(clientSocket.getOutputStream());
			    //BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			    InputStreamReader in = new InputStreamReader(clientSocket.getInputStream());
			    if(server) {
				    out.write("AnonFS "+Configuration.version+"\n");
				    out.flush();
			    }
			    while(true) {
			    	int count=0;
			    	byte line [] = readLine(in);
			    	if(line==null) {
			    		Core.logManager.log(this.getClass().getName(), "IP: " + clientSocket.getInetAddress().getHostAddress()  + " Client "+id+" disconnected!");
			    		return;
			    	}
			    	String message = new String(line);
			    	//Enable the below line to log every message
			    	//Core.logManager.log(this.getClass().getName(), "Got String:"+message);
			    	if(message.compareTo("info")==0) {
			    		Core.logManager.log(this.getClass().getName(), "ClientID:"+id+" asked info");
			    		out.write("AnonFS "+Configuration.version+"\n");
			    		out.flush();
			    	}
			    	else if(message.compareTo("GetPeerList")==0) {
			    		out.write("Not Implemented!"+"\n");
			    		out.flush();
			    		//TODO Implement GetPeerList
			    	}
			    	else if(message.startsWith("PushPiece")) {
			    		out.write("Not Implemented!"+"\n");
			    		out.flush();
			    		// TODO Implement PushPiece
			    	}
			    	else if(message.startsWith("GetPiece")) {
			    		out.write("Not Implemented!"+"\n");
			    		out.flush();
			    		// TODO Implement GetPiece
			    	}
			    	else if(message.startsWith("FindPiece")) {
			    		out.write("Not Implemented!"+"\n");
			    		out.flush();
			    		// TODO Implement FindPiece
			    	}
			    	else if(message.startsWith("MyIP")) {
			    		out.write(clientSocket.getInetAddress().getHostAddress()+"\n");
			    		out.flush();
			    	}
			    	else {
			    		out.write("Invalid command!"+"\n");
			    		out.flush();
			    	}
			    	// TODO write what to do here
			    }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}