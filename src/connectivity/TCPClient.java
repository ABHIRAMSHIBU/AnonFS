package connectivity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import algorithm.ByteArrayTransforms;
import configuration.Configuration;
import core.Core;
import datastructures.Packet;



public class TCPClient{
	SocketChannel socketChannel;
	String ip;
	int port;
	boolean failed=false;
	public boolean status=false,run=false;
	public void close() {
		try {
			socketChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/** 
	 * Sends request and gets reply using packet scheme
	 * @param data
	 * @throws IOException
	 */
//	public void sendRequest(String data) throws IOException {
//		Packet p = new Packet();
//		byte data_array[] = new byte[data.length()];
//		for(int i = 0; i<data.length() ; i++) {
//			data_array[i] = (byte)data.charAt(i);
//		}
//		HashMap<String, Object> packetWrapper=p.createPacket(data_array, Packet.REQUEST, 1, (byte) 0);
//		byte [] packet = (byte[]) packetWrapper.get("packet");
//		byte id = (byte) packetWrapper.get("id");
//		char packet_char[] = new char[packet.length];
//		for(int i = 0; i<packet.length;i++) {
//			packet_char[i]=(char)packet[i];
//		}
//		OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
//		InputStreamReader in = new InputStreamReader(socket.getInputStream());
//		out.write(packet_char);
//		out.flush();
//		System.out.println("Reading Packet");
//		Core.logManager.log(this.getClass().getName(), "Reading packet");
//		p.readInputStreamPacket(in);
//		System.out.println("Reading complete");
//		Core.logManager.log(this.getClass().getName(), "Reading complete");
//		System.out.println("Got data "+new String(ByteArrayTransforms.toCharArray(p.getDecodedData())));
//		// TODO: Remove reading request
//	}
	public TCPClient() {
		this.ip=Configuration.DEFAULT_IP;
		this.port=Configuration.DEFAULT_PORT;
		try {
			socketChannel = SocketChannel.open();
			InetSocketAddress ia = new InetSocketAddress(Configuration.DEFAULT_IP,Configuration.DEFAULT_PORT);
			socketChannel.connect(ia);
			TCPHander tcph = new TCPHander(socketChannel, Configuration.DEFAULT_PORT, false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			Core.logManager.log(getClass().getName(), "TCPClient Error occured for "+Configuration.DEFAULT_IP,4);
			status=false;
			e1.printStackTrace();
		}
	}
	public TCPClient(String ip) {
		this(ip,Configuration.DEFAULT_PORT);
	}
	public TCPClient(String ip, int port) {
		this.ip=ip;
		this.port=port;
		try {
			SocketChannel socketChannel = SocketChannel.open();
			InetSocketAddress ia = new InetSocketAddress(ip,port);
			socketChannel.connect(ia);
			TCPHander tcph = new TCPHander(socketChannel, port, false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			Core.logManager.log(getClass().getName(), "TCPClient Error occured for "+ip,4);
			status=false;
			e1.printStackTrace();
		}
	}
}
