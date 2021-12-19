package core;
import java.io.IOException;
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
import filesystem.PieceDiskStorage;
import logging.LogManager;

public class Core {
	public static LogManager logManager ;
	public static PeerDiscovery peerDiscovery;
	public static PeerDataHandler pdh;
	public static ConfigInit config;
	public static ConnectionIDHander cIDHandle;
	public static PeerUIDHander UIDHander;
    public static void runmain(String[] args) {
    	UIDHander = new PeerUIDHander();
    	logManager = new LogManager();
    	MetaDataHandler mdh = new MetaDataHandler();
    	
    	logManager.log(Core.class.getName(), "Welcome to AnonFS version: "+Configuration.version+" core now alive!");
    	logManager.log(Core.class.getName(), "UID:"+UIDHander.getUIDString());
    	cIDHandle = new ConnectionIDHander();
    	try {
			config = new ConfigInit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
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
    	
    	PieceDiskStorage pds = new PieceDiskStorage();
    	pds.diskinit();
    	try {
    		logManager.log(Core.class.getName(), "Writing Piece with checksum:"+new String(Base64.encode(pieces.get(0).checksum)));
    		pds.pieceToDisk(pieces.get(0));
    		Piece p = pds.diskToPiece(ByteArrayTransforms.toHexString(pieces.get(0).checksum));
    		logManager.log(Core.class.getName(), "Read piece with checksum:"+ new String(Base64.encode(p.checksum)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    	TCPServer tcpServer = new TCPServer(Configuration.DEFAULT_PORT);
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
