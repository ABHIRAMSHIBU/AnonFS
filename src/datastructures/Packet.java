package datastructures;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import algorithm.ByteArrayTransforms;
import core.Core;

public class Packet {
	byte id=0;
	//_variable are private variables which needs to be shared as they relate to last processed packet.
	byte _flags;
	byte _id;
	byte[] _data;
	int _size;
	int _padding;
	int _ttl;
	byte _refid;
	public static final byte REPLY=1;
	public static final byte REQUEST=0;
	/** createPakcket when given a byte array will generate a packet.
	 * 
	 * @param data - content of packet
	 * @param request - is it a request (0) or a reply(1)
	 * @param TTL - time to leave from network.
	 * @param refid - Reference id of the packet
	 * @return HashMap<String, Object> Valid Entries
	 *  (byte)"id", 
	 *  (byte)"refid", 
	 *  (int)"ttl", 
	 *  (byte)"request", 
	 *  (byte[])"body"
	 */
	public HashMap<String, Object> createPacket(byte[] data,byte request,int TTL,byte refid) {
		HashMap<String, Object> packetWrapper = new HashMap<String, Object>();
		byte base64Payload = 0;
		byte breakConnection = 0;
		byte flags = (byte) (request<<0 | base64Payload<<1 | breakConnection<<2);
		byte[] message = new byte[29+data.length];
		int index=0;
		//Core.logManager.log(this.getClass().getName(), "The original length is : "+(20+data.length) );
		// Set size
		byte sizeArray[] = new byte[8];
		for(int i=0;i<8;i++) {
			sizeArray[i]=(byte) ((0xFF<<(i*8) & ((long)data.length+29))>>(i*8));
			//Core.logManager.log(this.getClass().getName(), "Byte at "+i+" is "+Byte.toUnsignedInt(message[i]) );
		}
		long size=0;
		for(int i=0;i<8;i++) {
			size = size | (Byte.toUnsignedInt(sizeArray[i])<<(i*8));
			//Core.logManager.log(this.getClass().getName(), "Message["+i+"] is "+Byte.toUnsignedInt(message[i]));
		}
		Core.logManager.log(this.getClass().getName(), "Create Packet Decoded size: "+size);
		char  sizeHexArray[] = ByteArrayTransforms.toHexString(sizeArray).toCharArray();
		for(int i=0;i<16;i++) {
			message[i] = (byte)sizeHexArray[i];
		}
		index=16;
		// Set flags
		message[index++]=flags;
		// Set id
		message[index++]=id;
		message[index++]=refid;
		// Bug fix set ID before being altered
		packetWrapper.put("id", id);
		if(id==120) { //Increment ID rollover
			id=0;
		}
		else {
			id++;
		}
		// Set padding to 0, currently unused
		for(int i=0;i<8;i++) {
			message[index++]=0;
		}
		// Set TTL
		for(int i=0;i<2;i++) {
			message[index++]=(byte) ((0xFF<<(i*8) & (TTL))>>(i*8));
			//Core.logManager.log(this.getClass().getName(), "Byte at "+i+" is "+Byte.toUnsignedInt(message[i+18]));
		}
		// Set data
		for(int i=0;i<data.length;i++) {
			message[index++]=data[i];
		}
		packetWrapper.put("refid", refid);
		packetWrapper.put("ttl", TTL);
		packetWrapper.put("request", request);
		packetWrapper.put("packet", message);
		return packetWrapper;
	}
	/**decodePacket, decodes packet which is encoded by createPacket.
	 * 
	 * @param message - packet
	 * @return data -data in the packet
	 */
	public HashMap<String, Object> decodePacket(byte [] message) {
		HashMap<String, Object> packetWrapper = new HashMap<String, Object>();
		byte flags;
		int size=0;
		int ttl=0;
		int padding = 0;
		int index=0;
		//Core.logManager.log(this.getClass().getName(), "The original length is : "+(20+data.length) );
		// Get size
		char sizeHexChar[] = new char[16];
		for(int i=0;i<index+16;i++) {
			sizeHexChar[i] = (char)message[i];
		}
		index+=16;
		byte sizearr[] = ByteArrayTransforms.HexStringToBytes(new String(sizeHexChar));
		for(int i=0;i<8;i++) {
			size = size | (Byte.toUnsignedInt(sizearr[i])<<(i*8));
			//Core.logManager.log(this.getClass().getName(), "Message["+i+"] is "+Byte.toUnsignedInt(message[i]));
		}
		this._size=size;
		Core.logManager.log(this.getClass().getName(), "DecodePacket Size is "+size);
		byte[] data = new byte[size-29];
		this._data=data;
		// Get flags
		flags=message[index++];
		this._flags=flags;
		// Get id
		byte id=message[index++];
		byte refid=message[index++];
		Core.logManager.log(this.getClass().getName(), "ID is "+id);
		this._id=id;
		this._refid=refid;
		// Get padding 
		for(int i=0;i<8;i++) {
			padding = padding | (Byte.toUnsignedInt(message[index++])<<(i*8));
			//Core.logManager.log(this.getClass().getName(), "Message["+i+"] is "+Byte.toUnsignedInt(message[i+10]));
		}
		//Core.logManager.log(this.getClass().getName(), "Padding is "+padding);
		this._padding=padding;
		// Get TTL
		for(int i=0;i<2;i++) {
			ttl = ttl | (Byte.toUnsignedInt(message[index++])<<(i*8));
			//Core.logManager.log(this.getClass().getName(), "Message["+i+"] is "+Byte.toUnsignedInt(message[i+18]));
		}
		//Core.logManager.log(this.getClass().getName(), "ttl is "+ttl);
		this._ttl=ttl;
		// Get data
		for(int i=0;i<data.length;i++) {
			data[i]=message[index++];
		}
		this._data=data;
		packetWrapper.put("id", id);
		packetWrapper.put("refid", refid);
		packetWrapper.put("flags", flags);
		packetWrapper.put("body", data);
		return packetWrapper;
	}
	/**
	 * Get Time To Leave from network
	 * @return TTL last packet integer
	 */
	public int getDecodedTTL() {
		return this._ttl;
	}
	/**
	 * Get Packet Data
	 * @return Data in last packet byte array
	 */
	public byte[] getDecodedData() {
		return this._data;
	}
	/**
	 * Get Flags
	 * @return Flags of last packet byte array
	 */
	public byte getDecodedFlags() {
		return this._flags;
	}
	/**
	 * Get Packet ID
	 * @return ID of last packet decoded in byte
	 */
	public byte getDecodedID() {
		return this._id;
	}
	/**
	 * Get Packet ID
	 * @return ID of last packet created in byte
	 */
	public byte getCreatedID() {
		return (byte) ((this.id-1)%256);
	}
	/**
	 * Get Size of Packet
	 * @return Size of the packet in integer
	 */
	public int getDecodedSize() {
		return this._size;
	}
	/**
	 * Get Size of Padding
	 * @return Size of padding in last packet in integer
	 */
	public int getDecodedPadding() {
		return this._padding;
	}
	/**
	 * Get the reference ID of packet in case of reply
	 * @return Size of padding in last packet in integer
	 */
	public byte getDecodedrefid() {
		return this._refid;
	}
	/**
	 * Reads the packet from a socket InputStreamReader and decodes it
	 * @param in
	 * @return raw packet data as binary
	 * @throws IOException
	 */
	public HashMap<String, Object> readInputStreamPacket(SocketChannel socketChannel) throws IOException {
		try {
			// First 8 bytes are length so read it.
			ByteBuffer buffer = ByteBuffer.allocate(16);
			socketChannel.read(buffer);
			buffer.flip();
			byte sizearray[]=buffer.array();
			Core.logManager.log(this.getClass().getName(), "Size Buffer is :"+buffer);
			int size=0;
			byte sizeActual[] = ByteArrayTransforms.HexStringToBytes(new String(ByteArrayTransforms.toCharArray(sizearray)));
			for(int i=0;i<8;i++) {
				byte b = (byte)sizeActual[i];
				size = size | (Byte.toUnsignedInt(b)<<(i*8));
				//Core.logManager.log(this.getClass().getName(), "Message["+i+"] is "+Byte.toUnsignedInt(b));
			}
			Core.logManager.log(this.getClass().getName(), "Streamreader got size: "+size);
			if(size == 0) {
				return null;
			}
			//Read Packet Size.
			buffer = ByteBuffer.allocate(size);
			buffer.put(sizearray);
			socketChannel.read(buffer);
			byte packet[]=buffer.array();
//			in.read(packet,8,size-8);
//			for(int i=0;i<8;i++) {
//				packet[i]=sizearray[i];
//			}
			//Read the rest of the packet as we know the size.
			byte packet_byte[]=packet;
			HashMap<String, Object> packetWrapper = decodePacket((byte [])packet_byte);
			return packetWrapper;
		}
		catch(IndexOutOfBoundsException e) {
			Core.logManager.critical(this.getClass().getName(), "Got "+e.toString()+" treating as socket closed");
			return null;
		}
		catch(SocketException e) {
			Core.logManager.log(this.getClass().getName(), "Got "+e.toString()+" treating as socket closed",4);
			return null;
		}
	}
	/**
	 * Fetches request flag from the flags of last packet.
	 * @return - a byte request flag
	 */
	public byte getDecodedRequestFlag() {
		return (byte) (_flags & 0x01);
	}
	/**
	 * Fetches Base64Payload flag from the flags of last packet.
	 * @return - a byte Base64PayloadFlag flag
	 */
	public byte getDecodedBase64PayloadFlag() {
		return (byte) ((_flags & 0x02)>>1);
	}
	/**
	 * Fetches BreakConnection flag from the flags of last packet.
	 * @return - a byte BreakConnection flag
	 */
	public byte getDecodedBreakConnectionFlag() {
		return (byte) ((_flags & 0x04)>>2);
	}
}
