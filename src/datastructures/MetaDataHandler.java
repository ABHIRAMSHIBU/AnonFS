package datastructures;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import algorithm.ByteArrayTransforms;
import cryptography.SHA512;

public class MetaDataHandler {
	HashMap<Long, byte[]> table = new HashMap<Long, byte[]>();
	byte [] checksum = null;
	
	/**
	 * Inserts the checksum of a piece into the hashmap.
	 * @param piece
	 * @param seq
	 */
	public void insertEntry(byte[] checksum , long seq) {
		table.put(seq, checksum);
	}
	
	/**
	 * Retrieves the checksum from the hashmap.
	 * @param seq
	 * @return
	 */
	public byte[] getEntry(long seq) {
		return table.get(seq);
	}
	
	/**
	 * 
	 * @return
	 */
	public int size() {
		return table.size();
	}
	
	public String toString() {
		String metadataString = "";
		for(long i=0; i<table.size(); i++) {
			byte [] checksum = table.get(i);
			metadataString += ByteArrayTransforms.toHexString(checksum) + new String("\n"); 
		}
		return metadataString;
	}
	
	public void fromString(String input) throws Exception {
		if(table.size()!=0) {
			throw(new Exception("Metadata Hander is not Empty!"));
		}
		long entry = 0;
		int oldindex = -1;
		int index = input.indexOf("\n",oldindex+1);
		while(index!=-1) {
			// Read all char except \n
			String line = input.substring(oldindex+1,index);
			byte [] checksum = ByteArrayTransforms.HexStringToBytes(line);
			table.put(entry, checksum);
			entry++;
			oldindex = index;
			index = input.indexOf("\n",oldindex+1);
		}
	}
	
	public void calculateChecksum() {
		try {
			checksum = SHA512.digest(ByteArrayTransforms.toByteArray(toString()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte [] getChecksum() {
		if(checksum==null) {
			calculateChecksum();
		}
		return checksum;
	}
	
}
