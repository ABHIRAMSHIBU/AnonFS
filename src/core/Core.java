package core;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import algorithm.ByteArrayTransforms;
import algorithm.FileTransforms;
import configuration.ConfigInit;
import configuration.Configuration;
import connectivity.AutoConnector;
import connectivity.ConnectionIDHander;
import connectivity.PeerDiscovery;
import connectivity.TCPServer;
import cryptography.Base64;
import datastructures.MetaDataHandler;
import datastructures.PeerDataHandler;
import datastructures.Piece;
import filesystem.MetadataDiskStorage;
import filesystem.PieceDiskStorage;
import logging.LogManager;

public class Core {
	public static LogManager logManager ;
	public static PeerDiscovery peerDiscovery;
	public static PeerDataHandler pdh;
	public static ConfigInit config;
	public static ConnectionIDHander cIDHandle;
	public static PeerUIDHander UIDHander;
	public static PieceDiskStorage pieceDiskStorage;
	public static MetadataDiskStorage metadataDiskStorage;
	public static TradeHandler tradeHandler;
    public static void runmain(String[] args) {
    	try {
			config = new ConfigInit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	logManager = new LogManager();
    	UIDHander = new PeerUIDHander();
    	MetaDataHandler mdh = new MetaDataHandler();
    	pieceDiskStorage = new PieceDiskStorage();
    	metadataDiskStorage = new MetadataDiskStorage();
    	tradeHandler = new TradeHandler();
    	
    	logManager.log(Core.class.getName(), "Welcome to AnonFS version: "+Configuration.version+" core now alive!");
    	logManager.log(Core.class.getName(), "UID:"+UIDHander.getUIDString());
    	cIDHandle = new ConnectionIDHander();
    	
    	
    	pieceDiskStorage.diskinit();
    	metadataDiskStorage.diskinit();
    	
    	LinkedList<Piece> pieces = FileTransforms.FileToPices("/proc/cmdline", 40, mdh);
    	logManager.log(Core.class.getName(),"Pieces:"+new String(mdh.getEntry(0)));
    	
    	
    	pdh = new PeerDataHandler();
    	AutoConnector ac = new AutoConnector();
    	ac.start();
    	peerDiscovery = new PeerDiscovery();
    	peerDiscovery.start();
    	
    	Piece testpiece = new Piece();
    	testpiece.fromString(pieces.get(0).toString());
    	if(pieces.get(0).toString().equals(testpiece.toString())) {
    		logManager.log(Core.class.getName(), "Piece Serialization Test Success");
    	}
    	
//    	try {
//    		logManager.log(Core.class.getName(), "Writing Piece with checksum:"+new String(Base64.encode(pieces.get(0).checksum)));
//    		pds.pieceToDisk(pieces.get(0));
//    		Piece p = pds.diskToPiece(ByteArrayTransforms.toHexString(pieces.get(0).checksum));
//    		logManager.log(Core.class.getName(), "Read piece with checksum:"+ new String(Base64.encode(p.checksum)));
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	try {
    		pieceDiskStorage.piecesToDisk(pieces);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	try {
			metadataDiskStorage.metadataToDisk(mdh);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	
    	try {
			MetaDataHandler mdh1 =  metadataDiskStorage.diskToMetadata(new String(ByteArrayTransforms.toHexString(mdh.getChecksum())));
			if(mdh.toString().equals(mdh1.toString())) {
				System.out.println("Metadata dump, restore success!");
			}
			else {
				System.out.println("Metadata dump/restore failed!");
				System.out.println("mdh1="+mdh1.toString());
				System.out.println("mdh="+mdh.toString());
				
			}
    	} catch (ClassNotFoundException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	
    	try {
			LinkedList<Piece> piece_bkp = pieceDiskStorage.getPiecesWithMetaData(mdh);
			int count = 0;
			for(int i=0;i<pieces.size();i++) {
	    		if(pieces.get(i).toString().equals(piece_bkp.get(i).toString())) {
	    			count+=1;
	    		}
	    		else {
	    			logManager.critical(Core.class.getName(),"getPieces: Failed "+i);
	    			logManager.critical(Core.class.getName(),pieces.get(i).toString());
	    			logManager.critical(Core.class.getName(),piece_bkp.get(i).toString());
	    		}
	    	}
			if(count==pieces.size()) {
				logManager.log(Core.class.getName(),"getPieces: PASS");
			}
			else {
				logManager.log(Core.class.getName(),"getPieces: Failed");
			}
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	
    
    	// Not working so Message Data Handler is not serializable.
//    	try {
//			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("test"));
//			oos.writeObject(mdh);
//			oos.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	Core.logManager.log(Core.class.getName(), null);
    	CLIHandler cliHandler = new CLIHandler();
    	cliHandler.start();
		SocketInterfaceThread sit = new SocketInterfaceThread();
		sit.start();
    	TCPServer tcpServer = new TCPServer(Configuration.DEFAULT_PORT);
    	System.out.println("MAIN Thread EXIT!");
    }

    /**
     * Test Order
     * 
     * @param args
     */
	public static void runtests(String [] args) {
		
    	logManager = new LogManager();
    	System.out.println("Welcome to AnonFS version: "+Configuration.version+" core now in test mode.");
    	if(LogManager.failure==false) {
    		logManager.log(Core.class.getName(), "Welcome to AnonFS version: "+Configuration.version+" core now in test mode.");
    		System.out.println("Send hello through log manager, check AnonFSd.log");
    		logManager.log(Core.class.getName(),"Hello from test frame work!");
    		logManager.log(Core.class.getName(), "LogManager: PASS");
    	}
    	else {
    		System.out.println("LogManager Failed, exiting!");
    		return;
    	}
    	UIDHander = new PeerUIDHander();
    	if(UIDHander.getUIDString()!=null) {
    		logManager.log(Core.class.getName(), "PeerUIDHandler: PASS UID="+UIDHander.getUIDString());
    	}
    	else {
    		logManager.log(Core.class.getName(), "PeerUIDHandler: Failed UID="+UIDHander.getUIDString());
    	}
    	cIDHandle = new ConnectionIDHander();
    	try {
			config = new ConfigInit();
			if(config.failure == true) {
				throw new IOException();
			}
			logManager.log(Core.class.getName(), "ConfigInit: PASS");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logManager.log(Core.class.getName(), "ConfigInit: Failed");
		}
    	
    	MetaDataHandler mdh = new MetaDataHandler();
    	LinkedList<Piece> pieces = FileTransforms.FileToPices("test.jpg", 40, mdh);
    	if((new String(ByteArrayTransforms.toHexString(mdh.getEntry(0))))
    			.equals("804dfe56ca71266aa2ff6b69cf353b3e9f166162288d13d16c1b1b8119029fa727"
    					+ "6c5a6b4f336119c0ce2d269d4f526836b8ae59cfb33056988bc4f0e0af9848")) {
    		logManager.log(Core.class.getName(), "FileTransforms.FileToPieces: PASS");
    		logManager.log(Core.class.getName(), "MetaDataHandler: PASS");
    		logManager.log(Core.class.getName(), "SHA512: PASS");
    		logManager.log(Core.class.getName(), "ByteArrayTransforms.toHexString: PASS");
    	}
    	else {
    		logManager.log(Core.class.getName(), "FileTransforms.FileToPieces: Failed");
    		logManager.log(Core.class.getName(), "MetaDataHandler: Failed");
    		logManager.log(Core.class.getName(), "SHA512: Failed");
    		logManager.log(Core.class.getName(), "ByteArrayTransforms.toHexString: Failed");
    	}
    	Piece testpiece = new Piece();
    	testpiece.fromString(pieces.get(0).toString());
    	if(pieces.get(0).toString().equals(testpiece.toString())) {
    		logManager.log(Core.class.getName(), "Piece Serialization: PASS");
    	}
    	else {
    		logManager.log(Core.class.getName(), "Piece Serialization: Failed");
    	}
    	pdh = new PeerDataHandler();
    	if(pdh.failure == false) {
    		logManager.log(Core.class.getName(), "PeerDataHandler: PASS");
    	}
    	else {
    		logManager.log(Core.class.getName(), "PeerDataHandler: Failure");
    	}
    	
    	// MetaData disk handle test
    	metadataDiskStorage = new MetadataDiskStorage();
    	metadataDiskStorage.diskinit();
    	try {
			metadataDiskStorage.metadataToDisk(mdh);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	try {
			MetaDataHandler mdh1 =  metadataDiskStorage.diskToMetadata(new String(ByteArrayTransforms.toHexString(mdh.getChecksum())));
			if(mdh.toString().equals(mdh1.toString())) {
				logManager.log(Core.class.getName(),"Metadata dump, restore success!");
			}
			else {
				logManager.log(Core.class.getName(),"Metadata dump/restore failed!");
				logManager.log(Core.class.getName(),"mdh1="+mdh1.toString());
				logManager.log(Core.class.getName(),"mdh="+mdh.toString());
				
			}
    	} catch (ClassNotFoundException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	// AutoConnector Needs External TEST
    	// Peer Discovery Needs External TEST
    	// TCP Server Needs External TEST
    	logManager.log(Core.class.getName(), "Automated tests completed, proceed to manual tests... starting services");
    	AutoConnector ac = new AutoConnector();
    	ac.start();
    	peerDiscovery = new PeerDiscovery();
    	peerDiscovery.start();
    	TCPServer tcpServer = new TCPServer(Configuration.DEFAULT_PORT);
    	
	}
	public static void main(String [] args) {
		/*
		 * Test critical module functionality if testing is enabled.
		 */
		if(Configuration.test == false) {
			runmain(args);
		}
		else {
			
			runtests(args);
		}
	}
	
}
