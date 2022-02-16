package connectivity;

import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import core.Core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class TCPServer {
	int count = 0;
	static ServerSocket serverSocket;
	public TCPServer(int portNumber) {
		ServerSocketChannel serverSocketChannel;
		try {
			serverSocketChannel = ServerSocketChannel.open();
			SocketChannel socketChannel = null;
			serverSocketChannel.socket().bind(new InetSocketAddress(portNumber));
			while(true) {
				socketChannel = serverSocketChannel.accept();
				long id = Core.cIDHandle.genID();
		    	Core.logManager.log(this.getClass().getName(), "IP: " + socketChannel.getRemoteAddress() + " Accepted Connection "+count,2);
		    	(new TCPHander(socketChannel,count,true)).start();
		    	count++;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			Core.logManager.critical(this.getClass().getName(), "Unable to start server port:"+portNumber+" might be in use!");
			e1.printStackTrace();
		}
	}
}

