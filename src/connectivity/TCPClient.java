package connectivity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



public class TCPClient{
	Socket socket;
	String ip;
	int port;
	boolean failed=false;
	public boolean status=false,run=false;
	public void reconnect() {
		Socket pingSocket = null;
		try {
			socket.close();
		}
		catch(Exception e){
//			socket=null;
		}
		try {
				pingSocket = new Socket(ip, port);
				socket=pingSocket;
				status=true;
		} catch (IOException e) {
			System.out.println("TCPClient Error occured!");
			status=false;
		}
	}
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String echo(String data) {
		PrintWriter out = null;
		java.util.Scanner in = null;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new java.util.Scanner(new InputStreamReader(socket.getInputStream(), "ASCII"));
			in.useDelimiter("\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("TCPClient:IOError");
		}
		try {
			while(socket.getInputStream().available()>0) {
				System.out.println("Flushing Input");
				socket.getInputStream().read();
			}
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		out.println(data);
		String z="";
		try {
			int j=0;
			int retryCount=Configuration.FETCH_RETRY_COUNT;
			z="";
			boolean flag=false;
			while(j<retryCount) {
				if(socket.getInputStream().available()>0) {
					z += in.nextLine();
					flag=true;
				}
				if(flag) {
					break;
				}
					
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				j++;
			}
			if(z=="") {
				z="No input available";
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		run=true;
		return z;
	}
	public TCPClient() {
	
		Socket pingSocket = null;
	
			try {
					pingSocket = new Socket(Configuration.DEFAULT_IP, Configuration.DEFAULT_PORT);
					socket=pingSocket;
					status=true;
			} catch (IOException e) {
				System.out.println("TCPClient Error occured!");
				status=false;
			}
	}
	public TCPClient(String ip) {
		this(ip,Configuration.DEFAULT_PORT);
	}
	public TCPClient(String ip, int port) {
		this.ip=ip;
		this.port=port;
		Socket pingSocket = null;
	
			try {
					pingSocket = new Socket(ip, port);
					socket=pingSocket;
					status=true;
			} catch (IOException e) {
				System.out.println("TCPClient Error occured for "+ip);
				status=false;
			}
	}
}