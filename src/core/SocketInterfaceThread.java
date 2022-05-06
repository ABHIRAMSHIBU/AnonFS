package core;
// import sockets
import java.net.Socket;
import java.nio.Buffer;

import configuration.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;

public class SocketInterfaceThread extends Thread {
	@Override
	public void run() {
		super.run();
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(Configuration.DEFAULT_PORT+1);
			while (true) {
				Socket socket = serverSocket.accept();
				// create a new thread for each connection
				(new SocketInterface(socket)).start();


			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

class SocketInterface extends Thread {
	Socket socket;
	public SocketInterface(Socket socket) {
		this.socket = socket;
	}
	@Override
	public void run() {
		super.run();
		// read the data from the socket
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (true) {
			// log while loop start
			String data = "";
			try {
				data = in.readLine();	
				Core.logManager.log(SocketInterface.class.getName(),"data: "+data);		
				// if data is not null and upload
				if(data!=null && data.startsWith("upload")) {
					// get the filename after split
					String filename = data.split(" ")[1];
					Core.logManager.log(SocketInterface.class.getName(),"filename: "+filename+"Uploading...");
					String md5sum_mdh = Core.tradeHandler.uplink(filename);
					Core.logManager.log(SocketInterface.class.getName(),"md5sum_mdh: "+md5sum_mdh);
				}
				// if data is not null and download
				else if(data!=null && data.startsWith("download")) {
					// get the filename after split
					String filename = data.split(" ")[1];
					String metadataId = data.split(" ")[2];
					Core.logManager.log(SocketInterface.class.getName(),"filename: "+filename+"Downloading...");
					Core.tradeHandler.downlink(metadataId, filename);
				}
				// if null then terminate
				else if(data==null) {
					Core.logManager.log(SocketInterface.class.getName(),"data: null");
					break;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
}