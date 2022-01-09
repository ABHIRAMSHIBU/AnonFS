package datastructures;

import java.io.IOException;

import algorithm.ByteArrayTransforms;

public class Piece {
	
	public byte body[];
	public byte signature[];
	public byte checksum[];
	
	public byte[] getChecksum() {
		return checksum;
	}
	public void setChecksum(byte[] checksum) {
		this.checksum = checksum;
	}
	public byte[] getSignature() {
		return signature;
	}
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}
	public byte[] getBody() {
		return body;
	}
	public void setBody(byte[] body) {
		this.body = body;
	}
	
	/**
	 * Serializes a piece to a string
	 * @return serialized string of the piece
	 */
	public String toString() {
		byte [] signature = this.signature;
		if(signature == null) {
			signature = new byte[0];
		}
		int lengthofWholePacket = 76 + signature.length + body.length;
		byte message[] = new byte[lengthofWholePacket];
		// Writing signature
		for(int i=0;i<4;i++) {
			message[i]=(byte) ((0xFF<<(i*8) & ((int)signature.length))>>(i*8));
		}
		// Leaving 8 bytes for future
		
		// Copy checksum 64 bytes
		for(int i=0;i<checksum.length;i++) {
			message[i+12] = checksum[i];
		}
		
		// Copy signature
		for(int i=0;i<signature.length;i++) {
			message[i+12+checksum.length] = signature[i];
		}
		
		// Copy Body
		for(int i=0;i<body.length;i++) {
			message[i+12+checksum.length+signature.length] = body[i];
		}
		
		return ByteArrayTransforms.toHexString(message);
	}
	
	/**
	 * Deserializes a piece from a string
	 * @return
	 */
	public void fromString(String in) {
//		System.out.println("MESSAGE:"+in);
		byte [] message = ByteArrayTransforms.HexStringToBytes(in);
		int signaturelength = 0;
		
		for(int i=0;i<4;i++) {
			signaturelength = signaturelength | (Byte.toUnsignedInt(message[i])<<(i*8));
			//Core.logManager.log(this.getClass().getName(), "Message["+i+"] is "+Byte.toUnsignedInt(message[i]));
		}
		
		// Allocating space
		signature = new byte[signaturelength];
		
		checksum = new byte[64];
		
		body = new byte[message.length - checksum.length - signaturelength - 12];
		
		// Copy checksum 64 bytes
		for(int i=0;i<checksum.length;i++) {
			 checksum[i] = message[i+12] ;
		}
		
		// Copy signature
		for(int i=0;i<signature.length;i++) {
			 signature[i] = message[i+12+checksum.length];
		}
		
		// Copy Body
		for(int i=0;i<(message.length - checksum.length - signaturelength - 12);i++) {
			body[i] = message[i+12+checksum.length+signature.length];
		}
	}
}
