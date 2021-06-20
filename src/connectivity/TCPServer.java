package connectivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;

public class TCPServer {
	int count = 0;
	static ServerSocket serverSocket;
	public TCPServer(int portNumber) {
		try { 
			    serverSocket = new ServerSocket(portNumber);
			    while(true) {
			    	Socket clientSocket = serverSocket.accept();
			    	System.out.println("Accepted connection "+count);
			    	(new TCPServerClientHandler(clientSocket,count)).start();
			    	count++;
			    }
			}
		catch (Exception e) {
			System.out.println("Starting server error!");
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
					System.out.println("Thread starting with id "+id);
				    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				    out.println("SSAL Command line Ready!");
				    while(true) {
				    	String message = in.readLine();
				    	if(message==null) {
				    		System.out.println("Client "+id+" disconnect!");
				    		return;
				    	}
				    	// TODO write what to do here
				    }
			}
			catch (Exception e) {
				System.out.println("Client telnet handler crash... Restarting..");
				run();
			}
		}
}
