package datastructures;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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

	public byte[] toChecksumBytes() {
		byte[] linearbyetes = new byte[table.size()*65];
		// loop through table using foreach
		table.entrySet().parallelStream().forEach(arg0 -> {
			byte[] checksum = arg0.getValue();
			System.arraycopy(checksum, 0, linearbyetes, arg0.getKey().intValue()*65, 64);
			linearbyetes[arg0.getKey().intValue()*65+64] = (byte) 0x0D;
		});
		return linearbyetes;
	}

	public void fromCheckSumBytes(byte[] checksumBytes) {
		for(int i=0;i<checksumBytes.length/65;i+=1) {
			byte[] checksum = new byte[64];
			System.arraycopy(checksumBytes, i*65, checksum, 0, 64);
			table.put((long) i, checksum);
		}

	};
	
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
			checksum = SHA512.digest(toChecksumBytes());
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
