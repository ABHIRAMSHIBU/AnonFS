package core;
import java.io.IOException;

import configuration.ConfigInit;
import logging.LogManager;

public class Core {
	public static LogManager logManager ;
    public static void main(String[] args) {
    	logManager = new LogManager();
    	try {
			new ConfigInit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
