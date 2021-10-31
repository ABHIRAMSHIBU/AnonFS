package filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;

import algorithm.ByteArrayTransforms;
import algorithm.FileTransforms;
import cryptography.Base64;

import core.Core;
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
	 * @param p
	 * @return True if write was success False if not.
	 */
	public boolean pieceToDisk(Piece p) {
		return false;
	}
	
	/**
	 * Reads the piece from the disk and constructs the datastructure.
	 * @return Piece if read success, null if read failure or checksum failure.
	 */
	public Piece diskToPiece() {
		return null;
	}
}
