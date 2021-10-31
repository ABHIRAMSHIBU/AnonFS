package datastructures;

import java.util.HashMap;

public class MetaDataHandler {
	HashMap<Long, byte[]> table = new HashMap<Long, byte[]>();
	
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
	
}
