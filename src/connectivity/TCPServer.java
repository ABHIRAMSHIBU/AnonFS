package connectivity;

import java.net.Socket;

import core.Core;
import java.net.ServerSocket;

public class TCPServer {
	int count = 0;
	static ServerSocket serverSocket;
	public TCPServer(int portNumber) {
		try { 
			    serverSocket = new ServerSocket(portNumber);
			    while(true) {
			    	Socket clientSocket = serverSocket.accept();
			    	Core.logManager.log(this.getClass().getName(), "IP: " + clientSocket.getInetAddress().getHostAddress() + " Accepted Connection "+count);
			    	(new TCPHander(clientSocket,count,true)).start();
			    	count++;
			    }
			}
		catch (Exception e) {
			Core.logManager.critical(this.getClass().getName(), "Unable to start server port:"+portNumber+" might be in use!");
		}
	}
}

