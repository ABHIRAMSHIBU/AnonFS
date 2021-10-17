package core;
import java.io.IOException;

import algorithm.FileTransforms;
import configuration.ConfigInit;
import configuration.Configuration;
import connectivity.AutoConnector;
import connectivity.ConnectionIDHander;
import connectivity.TCPServer;
import datastructures.PeerDataHandler;
import logging.LogManager;

public class Core {
	public static LogManager logManager ;
	public static PeerDataHandler pdh;
	public static ConfigInit config;
	public static ConnectionIDHander cIDHandle;
	public static PeerUIDHander UIDHander;
    public static void main(String[] args) {
    	UIDHander = new PeerUIDHander();
    	logManager = new LogManager();
    	FileTransforms.FileToPices("/proc/cmdline",40);
    	logManager.log(Core.class.getName(), "Welcome to AnonFS version: "+Configuration.version+" core now alive!");
    	logManager.log(Core.class.getName(), "UID:"+UIDHander.getUIDString());
    	cIDHandle = new ConnectionIDHander();
    	try {
			config = new ConfigInit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	pdh = new PeerDataHandler();
    	AutoConnector ac = new AutoConnector();
    	ac.start();
    	TCPServer tcpServer = new TCPServer(Configuration.DEFAULT_PORT);
    }
}
