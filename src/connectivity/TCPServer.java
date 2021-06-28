package connectivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import configuration.Configuration;
import core.Core;
import logging.LogManager;

import java.net.ServerSocket;

public class TCPServer {
	int count = 0;
	static ServerSocket serverSocket;
	public TCPServer(int portNumber) {
		try { 
			    serverSocket = new ServerSocket(portNumber);
			    while(true) {
			    	Socket clientSocket = serverSocket.accept();
			    	Core.logManager.log(this.getClass().getName(), "Accepted Connection "+count);
			    	(new TCPServerClientHandler(clientSocket,count)).start();
			    	count++;
			    }
			}
		catch (Exception e) {
			Core.logManager.critical(this.getClass().getName(), "Unable to start server port:"+portNumber+" might be in use!");
		}
	}
}
class TCPServerClientHandler extends Thread {
		Socket clientSocket;
		int id;
		TCPServerClientHandler(Socket clientSocket,int id) {
			this.clientSocket=clientSocket;
			this.id = id;
		}
		public void run() {
			try { 
				    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				    out.println("AnonFS "+Configuration.version);
				    while(true) {
				    	String message = in.readLine();
				    	if(message==null) {
				    		Core.logManager.log(this.getClass().getName(), "Client "+id+" disconnected!");
				    		return;
				    	}
				    	if(message.compareTo("info")==0) {
				    		Core.logManager.log(this.getClass().getName(), "ClientID:"+id+" asked info");
				    		out.println("AnonFS "+Configuration.version);
				    	}
				    	else if(message.compareTo("GetPeerList")==0) {
				    		out.println("Not Implemented!");
				    		//TODO Implement GetPeerList
				    	}
				    	else if(message.startsWith("PushPiece")) {
				    		out.println("Not Implemented!");
				    		// TODO Implement PushPiece
				    	}
				    	else if(message.startsWith("GetPiece")) {
				    		out.println("Not Implemented!");
				    		// TODO Implement GetPiece
				    	}
				    	else if(message.startsWith("FindPiece")) {
				    		out.println("Not Implemented!");
				    		// TODO Implement FindPiece
				    	}
				    	else if(message.startsWith("MyIP")) {
				    		out.println(clientSocket.getInetAddress().getHostAddress());
				    	}
				    	else {
				    		out.println("Invalid command!");
				    	}
				    	// TODO write what to do here
				    }
			}
			catch (Exception e) {
				run();
			}
		}
}
