package core;
import java.io.IOException;

import configuration.ConfigInit;
import configuration.Configuration;
import connectivity.AutoConnector;
import connectivity.TCPServer;
import datastructures.PeerDataHandler;
import logging.LogManager;

public class Core {
	public static LogManager logManager ;
	public static PeerDataHandler pdh;
	public static ConfigInit config;
    public static void main(String[] args) {
    	logManager = new LogManager();
    	logManager.log(Core.class.getName(), "Welcome to AnonFS version: "+Configuration.version+" core now alive!");
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
