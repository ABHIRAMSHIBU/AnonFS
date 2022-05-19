package logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import configuration.ConfigInit;
import core.Core;

public class LogManager {
	private static int count=0;
	static FileHandler fh;
	static SimpleFormatter formatter;
	static Logger log = Logger.getLogger("");
	public static boolean failure = true;
	public LogManager() {
		try {
			LogManager.fh = new FileHandler("AnonFSd.log");
			LogManager.formatter= new SimpleFormatter();  
	        LogManager.fh.setFormatter(formatter);
	        try {
				LogManager.log.addHandler(fh);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.setProperty("java.util.logging.SimpleFormatter.format",
	              "[%1$tF %1$tT] [%4$-7s] %5$s %n");
		failure = false;
	}
	public LogManager(String logFile) {
		try {
			LogManager.fh = new FileHandler(logFile);
			LogManager.formatter= new SimpleFormatter();  
	        LogManager.fh.setFormatter(formatter);
	        try {
				log.addHandler(fh);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.setProperty("java.util.logging.SimpleFormatter.format",
	              "[%1$tF %1$tT] [%4$-7s] %5$s %n");
		failure = false;
	}
	public void log(String class_name,String message, int level) {
		if(Core.config.getLogLevel() >= level) {
			log.log(Level.INFO,"ID:"+count+" Class:"+class_name+" "+message);
		}
	}
	public void log(String class_name,String message) {
		if(Core.config.getLogLevel()>0) {
			log.log(Level.INFO,"ID:"+count+" Class:"+class_name+" "+message);
			count+=1;
		}
	}
	public void critical(String class_name,String message) {
		log.log(Level.SEVERE,"ID:"+count+" Class:"+class_name+" "+message);
		count+=1;
	}
	public int getLogCount() {
		return count;
	}
}
