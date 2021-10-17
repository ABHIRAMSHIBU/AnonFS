package algorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import core.Core;
import datastructures.Piece;

public class FileTransforms {
	// TODO: Strip Metadata and add shasum.
	/** 
	 * Read a file segment it to pieces and return pieces. Will need signature in future
	 * for signing. If any error occured like IOError it will return null
	 * @param filepath - Path of the file to be segmented
	 * @param max_piece_size - Size of each piece at max.
	 * @return LinkedList with Pieces.
	 */
	public static LinkedList<Piece> FileToPices(String filepath,int max_piece_size) {
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
					p.seqnum = seq;
					pieces.add(p);
					seq++;
				}
			}
			fis.close();
		} catch (IOException e) {
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
}
