package filesystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;

import algorithm.ByteArrayTransforms;
import core.Core;
import cryptography.Base64;
import cryptography.SHA512;
import datastructures.MetaDataHandler;
import datastructures.Piece;

public class MetadataDiskStorage {
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
		
		f = new File(storageLocation+"/metadata");
		
		// Create the directory if it don't exist already.
		if(f.exists() && f.isDirectory()) {
			Core.logManager.log(this.getClass().getName(),"Found existing directory "+Paths.get(storageLocation,"metadata"),3);
		}
		else {
			f.mkdir();
			Core.logManager.log(this.getClass().getName(), "Made directory "+Paths.get(storageLocation,"metadata"),3);
		}
		// At this point we know that the directory store/pieces exists.
		
		// Disk init success. Now we have the directories needed.
	}
	
	/**
	 * Writes a metadata to disk
	 * @param p - Piece to write to disk.
	 * @return True if write was success False if not.
	 * @throws IOException 
	 */
	public boolean metadataToDisk(MetaDataHandler mdh) throws IOException {
		mdh.calculateChecksum();
		String checksum = ByteArrayTransforms.toHexString(mdh.getChecksum());
		Core.logManager.log(this.getClass().getName(),"Writing file with checksum base64 encoded as: "+checksum,3);
		File outputFile = new File((Paths.get(storageLocation,"metadata",checksum+".bin").toString()));
		if(outputFile.exists()) {
			Core.logManager.log(this.getClass().getName(), "File already exists!",4);
			return false;
		}
		else {
			Core.logManager.log(this.getClass().getName(), "File does not  exist, writing",4);
			BufferedWriter fileOut = new BufferedWriter(new FileWriter(outputFile));
			fileOut.write(mdh.toString()+"\n");
			
//			FileOutputStream fileOut = new FileOutputStream(outputFile);
//			ObjectOutputStream objectToFile = new ObjectOutputStream(fileOut);
//			objectToFile.writeObject(p);
//			objectToFile.close();
			fileOut.close();
			return true;
		}
	}
	
	/**
	 * Reads the metadata from the disk and constructs the datastructure.
	 * @param checksum
	 * @return metadata if read success, null if read failure or checksum failure.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public MetaDataHandler diskToMetadata(String checksum) throws ClassNotFoundException, IOException {
		File inputFile = new File((Paths.get(storageLocation,"metadata",checksum+".bin").toString()));
		if(! inputFile.exists()) {
			return null;
		}
		else {
			BufferedReader fileIn = new BufferedReader(new FileReader(inputFile));
			MetaDataHandler mdh = new MetaDataHandler();
			String data = "";
			while(true) {
				String line = fileIn.readLine();
				if(line.equals("")) {
					break;
				}
				data+=line+"\n";
			}
			try {
				mdh.fromString(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			fileIn.close();
			Core.logManager.log(this.getClass().getName(),"Read file with checksum base64 encoded as: "+new String(Base64.encode(mdh.getChecksum())),3);
			return mdh;
		}
	}
	// TODO: Need to add timetolive
}
