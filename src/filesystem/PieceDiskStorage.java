package filesystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.LinkedList;

import algorithm.ByteArrayTransforms;
import algorithm.FileTransforms;
import cryptography.Base64;

import core.Core;
import datastructures.MetaDataHandler;
import datastructures.Piece;

public class PieceDiskStorage {
	File f;
	String storageLocation ;
	/**
	 * Initializes the disk directory structure.
	 */
	public void diskinit() {
		
		storageLocation = Core.config.getPeerRoot();
		
		f = new File(storageLocation);
		
		// Create the directory if it don't exist already.
		if(f.exists() && f.isDirectory()) {
			Core.logManager.log(this.getClass().getName(), "Found existing directory "+storageLocation,3);
		}
		else {
			f.mkdir();
			Core.logManager.log(this.getClass().getName(), "Made directory "+storageLocation,3);
		}
		// At this point we know that the directory store exists.
		
		f = new File(storageLocation+"/pieces");
		
		// Create the directory if it don't exist already.
		if(f.exists() && f.isDirectory()) {
			Core.logManager.log(this.getClass().getName(),"Found existing directory "+Paths.get(storageLocation,"pieces"),3);
		}
		else {
			f.mkdir();
			Core.logManager.log(this.getClass().getName(), "Made directory "+Paths.get(storageLocation,"pieces"),3);
		}
		// At this point we know that the directory store/pieces exists.
		
		// Disk init success. Now we have the directories needed.
	}
	
	/**
	 * Writes a piece to disk
	 * @param p - Piece to write to disk.
	 * @return True if write was success False if not.
	 * @throws IOException 
	 */
	public boolean pieceToDisk(Piece p) throws IOException {
		String checksum = ByteArrayTransforms.toHexString(p.checksum);
		Core.logManager.log(this.getClass().getName(),"Writing file with checksum base64 encoded as: "+checksum,3);
		File outputFile = new File((Paths.get(storageLocation,"pieces",checksum+".bin").toString()));
		if(outputFile.exists()) {
			Core.logManager.log(this.getClass().getName(), "File already exists!",4);
			return false;
		}
		else {
			Core.logManager.log(this.getClass().getName(), "File does not  exist, writing",4);
			BufferedWriter fileOut = new BufferedWriter(new FileWriter(outputFile));
			fileOut.write(p.toString()+"\n");
			
//			FileOutputStream fileOut = new FileOutputStream(outputFile);
//			ObjectOutputStream objectToFile = new ObjectOutputStream(fileOut);
//			objectToFile.writeObject(p);
//			objectToFile.close();
			fileOut.close();
			return true;
		}
	}
	
	/**
	 * Reads the piece from the disk and constructs the datastructure.
	 * @param checksum
	 * @return Piece if read success, null if read failure or checksum failure.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public Piece diskToPiece(String checksum) throws ClassNotFoundException, IOException {
		File inputFile = new File((Paths.get(storageLocation,"pieces",checksum+".bin").toString()));
		if(! inputFile.exists()) {
			return null;
		}
		else {
			BufferedReader fileIn = new BufferedReader(new FileReader(inputFile));
			Piece p = new Piece();
			p.fromString(fileIn.readLine());  
			fileIn.close();
			Core.logManager.log(this.getClass().getName(),"Read file with checksum base64 encoded as: "+new String(Base64.encode(p.checksum)),3);
			return p;
		}
	}
	// TODO: Need to add timetolive
	
	public boolean piecesToDisk(LinkedList<Piece> pieces) throws IOException {
		boolean ret = true;
		for(int i=0; i<pieces.size(); i++) {
			ret = ret && pieceToDisk(pieces.get(i));
		}
		return ret;
	}
	
	public LinkedList<Piece> getPiecesWithMetaData(MetaDataHandler mdh) throws ClassNotFoundException, IOException {
		LinkedList<Piece> ret = new LinkedList<Piece>();
		for(int i=0;i<mdh.size();i++) {
			byte [] checksum = mdh.getEntry(i);
			ret.add(i,diskToPiece(new String(ByteArrayTransforms.toHexString(checksum))));
		}
		return ret;
	}
}
