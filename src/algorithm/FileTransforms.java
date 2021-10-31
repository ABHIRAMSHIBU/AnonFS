package algorithm;

import java.io.File;
import cryptography.SHA512;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

import core.Core;
import datastructures.MetaDataHandler;
import datastructures.Piece;

public class FileTransforms {
	// TODO: Strip Metadata and add shasum.
	/** 
	 * Read a file segment it to pieces and return pieces. Will need signature in future
	 * for signing. If any error occured like IOError it will return null
	 * @param filepath - Path of the file to be segmented
	 * @param max_piece_size - Size of each piece at max.
	 * @param mdh - Metadata output object.
	 * @return LinkedList with Pieces.
	 */
	public static LinkedList<Piece> FileToPices(String filepath, int max_piece_size, MetaDataHandler mdh) {
		LinkedList<Piece> pieces = null;
		long seq = 0;
		try {
			File inputfile = new File(filepath);
			FileInputStream fis = new FileInputStream(inputfile);
			pieces = new LinkedList<Piece>();
			while(true) {
				byte [] line = fis.readNBytes(max_piece_size);
				if(line.length==0) {
					// No more to read	
					break;
				}
				else {
					// Data Available in line
					Piece p = new Piece();
					p.body = line;
					p.checksum = SHA512.digest(line);
					mdh.insertEntry(p.checksum, seq);
					
//					p.seqnum = seq;
					pieces.add(p);
					seq++;
				}
			}
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pieces;
	}
	
	/**
	 * Takes a list of Pieces and Generates a File assuming Piece is in correct order.
	 * @param filepath - Path of the output file
	 * @param pieces - LinkedList of Pieces.
	 */
	public static void PiecesToFile(String filepath, LinkedList<Piece> pieces) {
		try {
			File outputfile = new File(filepath);
			FileOutputStream fos = new FileOutputStream(outputfile);
			for(int i=0; i<pieces.size();i++) {
				Piece p = pieces.get(i);
				fos.write(p.body);
			}
			fos.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ReorderPieces According to the metadata.
	 * @param pieces
	 * @param mdh
	 * @return
	 */
	public static LinkedList<Piece> ReorderPieces(LinkedList<Piece> pieces, MetaDataHandler mdh){
		LinkedList<Piece> OrderedPieces = new LinkedList<Piece>();
		for(int i=0;i<mdh.size();i++) {
			byte [] checksum =  mdh.getEntry(i);
			for(int j=0;j<pieces.size();j++) {
				if(pieces.get(j).getChecksum() == checksum) {
					OrderedPieces.add(pieces.get(j));
					break;
				}
			}
		}
		return OrderedPieces;
	}
	
	/**
	 * Verifies the list of Pieces.
	 * @param pieces
	 * @param mdh
	 * @return
	 */
	public static boolean VerifyPieces(LinkedList<Piece> pieces){
		for(int i=0;i<pieces.size();i++) {
			byte [] checksum =  pieces.get(i).getChecksum();
			try {
				if(! SHA512.digest(pieces.get(i).body).equals(checksum)) {
					return false;
				}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	
	/**
	 * Verifies an individual piece.
	 * @param p
	 * @return
	 */
	public static boolean VerifyPiece(Piece p) {
		try {
			if(p.checksum.equals(SHA512.digest(p.body))) {
				return true;
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
