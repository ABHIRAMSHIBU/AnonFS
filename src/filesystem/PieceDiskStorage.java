package filesystem;

import java.io.File;

import core.Core;
import datastructures.Piece;

public class PieceDiskStorage {
	File f;
	
	/**
	 * Initializes the disk directory structure.
	 */
	public void diskinit() {
		
		f = new File("store");
		
		// Create the directory if it don't exist already.
		if(f.exists() && f.isDirectory()) {
			Core.logManager.log(this.getClass().getName(), "Found existing directory ./store");
		}
		else {
			f.mkdir();
			Core.logManager.log(this.getClass().getName(), "Found existing directory ./store");
		}
		// At this point we know that the directory store exists.
		
		f = new File("store/pieces");
		
		// Create the directory if it don't exist already.
		if(f.exists() && f.isDirectory()) {
			Core.logManager.log(this.getClass().getName(),"Found existing directory ./store/pieces");
		}
		else {
			f.mkdir();
			Core.logManager.log(this.getClass().getName(), "Found existing directory ./store/pieces");
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
