package datastructures;

import java.io.IOException;
import java.io.InputStreamReader;

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
	public static final byte REPLY=1;
	public static final byte REQUEST=0;
	/** createPakcket when given a byte array will generate a packet.
	 * 
	 * @param data - content of packet
	 * @param request - is it a request (0) or a reply(1)
	 * @param TTL - time to leave from network.
	 * @return packet
	 */
	public byte[] createPacket(byte[] data,byte request,int TTL) {
		byte base64Payload = 0;
		byte breakConnection = 0;
		byte flags = (byte) (request<<0 | base64Payload<<1 | breakConnection<<2);
		byte[] message = new byte[20+data.length];
		//Core.logManager.log(this.getClass().getName(), "The original length is : "+(20+data.length) );
		// Set size
		for(int i=0;i<8;i++) {
			message[i]=(byte) ((0xFF<<(i*8) & ((long)data.length+20))>>(i*8));
			//Core.logManager.log(this.getClass().getName(), "Byte at "+i+" is "+Byte.toUnsignedInt(message[i]) );
		}
		// Set flags
		message[8]=flags;
		// Set id
		message[9]=id;
		if(id==255) { //Increment ID rollover
			id=0;
		}
		else {
			id++;
		}
		// Set padding to 0, currently unused
		for(int i=0;i<8;i++) {
			message[i+10]=0;
		}
		// Set TTL
		for(int i=0;i<2;i++) {
			message[i+18]=(byte) ((0xFF<<(i*8) & (TTL))>>(i*8));
			//Core.logManager.log(this.getClass().getName(), "Byte at "+i+" is "+Byte.toUnsignedInt(message[i+18]));
		}
		// Set data
		for(int i=0;i<data.length;i++) {
			message[i+20]=data[i];
		}
		return message;
	}
	/**decodePacket, decodes packet which is encoded by createPacket.
	 * 
	 * @param message - packet
	 * @return data -data in the packet
	 */
	public byte[] decodePacket(byte [] message) {
		byte flags;
		int size=0;
		int ttl=0;
		int padding = 0;
		//Core.logManager.log(this.getClass().getName(), "The original length is : "+(20+data.length) );
		// Get size
		for(int i=0;i<8;i++) {
			size = size | (Byte.toUnsignedInt(message[i])<<(i*8));
			Core.logManager.log(this.getClass().getName(), "Message["+i+"] is "+Byte.toUnsignedInt(message[i]));
		}
		this._size=size;
		Core.logManager.log(this.getClass().getName(), "Size is "+size);
		byte[] data = new byte[size-20];
		this._data=data;
		// Get flags
		flags=message[8];
		this._flags=flags;
		// Get id
		byte id=message[9];
		Core.logManager.log(this.getClass().getName(), "ID is "+id);
		this._id=id;
		// Get padding 
		for(int i=0;i<8;i++) {
			padding = padding | (Byte.toUnsignedInt(message[i+10])<<(i*8));
			Core.logManager.log(this.getClass().getName(), "Message["+i+"] is "+Byte.toUnsignedInt(message[i+10]));
		}
		Core.logManager.log(this.getClass().getName(), "Padding is "+padding);
		this._padding=padding;
		// Get TTL
		for(int i=0;i<2;i++) {
			ttl = ttl | (Byte.toUnsignedInt(message[i+18])<<(i*8));
			Core.logManager.log(this.getClass().getName(), "Message["+i+"] is "+Byte.toUnsignedInt(message[i+18]));
		}
		Core.logManager.log(this.getClass().getName(), "ttl is "+ttl);
		this._ttl=ttl;
		// Get data
		for(int i=0;i<data.length;i++) {
			data[i]=message[i+20];
		}
		this._data=data;
		return data;
	}
	/**
	 * Get Time To Leave from network
	 * @return TTL last packet integer
	 */
	public int getTTL() {
		return this._ttl;
	}
	/**
	 * Get Packet Data
	 * @return Data in last packet byte array
	 */
	public byte[] getData() {
		return this._data;
	}
	/**
	 * Get Flags
	 * @return Flags of last packet byte array
	 */
	public byte getFlags() {
		return this._flags;
	}
	/**
	 * Get Packet ID
	 * @return ID of last packet in byte
	 */
	public byte getID() {
		return this._id;
	}
	/**
	 * Get Size of Packet
	 * @return Size of the packet in integer
	 */
	public int getSize() {
		return this._size;
	}
	/**
	 * Get Size of Padding
	 * @return Size of padding in last packet in integer
	 */
	public int getPadding() {
		return this._padding;
	}
	/**
	 * Reads the packet from a socket InputStreamReader and decodes it
	 * @param in
	 * @return raw packet data as binary
	 * @throws IOException
	 */
	public byte [] readInputStreamPacket(InputStreamReader in) throws IOException {
		// First 8 bytes are length so read it.
		char sizearray[]=new char[8];
		//Core.logManager.log(this.getClass().getName(), "Reading");
		in.read(sizearray,0,8);
		//Core.logManager.log(this.getClass().getName(), "Read");
		int size=0;
		for(int i=0;i<8;i++) {
			byte b = (byte)sizearray[i];
			size = size | (Byte.toUnsignedInt(b)<<(i*8));
			//Core.logManager.log(this.getClass().getName(), "Message["+i+"] is "+Byte.toUnsignedInt(b));
		}
		//Core.logManager.log(this.getClass().getName(), "Got size "+size);
		char packet[]=new char[size];
		in.read(packet,8,size-8);
		for(int i=0;i<8;i++) {
			packet[i]=sizearray[i];
		}
		byte packet_byte[]=new byte[size];
		for(int i=0;i<size;i++) {
			packet_byte[i]=(byte)packet[i];
		}
		decodePacket((byte [])packet_byte);
		return packet_byte;
	}
	/**
	 * Fetches request flag from the flags of last packet.
	 * @return - a byte request flag
	 */
	public byte getRequestFlag() {
		return (byte) (_flags & 0x01);
	}
}
