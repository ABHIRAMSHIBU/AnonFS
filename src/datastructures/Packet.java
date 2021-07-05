package datastructures;

import core.Core;

public class Packet {
	byte id=0;
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
	public byte[] decodePacket(byte [] message) {
		byte flags;
		int size=0;
		int ttl=0;
		int padding = 0;
		//Core.logManager.log(this.getClass().getName(), "The original length is : "+(20+data.length) );
		// Set size
		for(int i=0;i<8;i++) {
			size = size | (Byte.toUnsignedInt(message[i])<<(i*8));
			Core.logManager.log(this.getClass().getName(), "Message["+i+"] is "+Byte.toUnsignedInt(message[i]));
		}
		Core.logManager.log(this.getClass().getName(), "Size is "+size);
		byte[] data = new byte[size-20];
		// Set flags
		flags=message[8];
		// Set id
		int id=message[9];
		Core.logManager.log(this.getClass().getName(), "ID is "+id);
		// Set padding to 0, currently unused
		for(int i=0;i<8;i++) {
			message[i+10]=0;
		}
		for(int i=0;i<8;i++) {
			padding = padding | (Byte.toUnsignedInt(message[i+10])<<(i*8));
			Core.logManager.log(this.getClass().getName(), "Message["+i+"] is "+Byte.toUnsignedInt(message[i+10]));
		}
		Core.logManager.log(this.getClass().getName(), "Padding is "+padding);
		// Set TTL
		for(int i=0;i<2;i++) {
			ttl = ttl | (Byte.toUnsignedInt(message[i+18])<<(i*8));
			Core.logManager.log(this.getClass().getName(), "Message["+i+"] is "+Byte.toUnsignedInt(message[i+18]));
		}
		Core.logManager.log(this.getClass().getName(), "ttl is "+ttl);
		// Set data
		for(int i=0;i<data.length;i++) {
			data[i]=message[i+20];
		}
		return data;
	}
}