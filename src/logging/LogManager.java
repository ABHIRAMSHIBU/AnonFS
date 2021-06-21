package logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogManager {
	private static int count=0;
	static FileHandler fh;
	public LogManager() {
		try {
			fh = new FileHandler("AnonFSd.log");
			SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.setProperty("java.util.logging.SimpleFormatter.format",
	              "[%1$tF %1$tT] [%4$-7s] %5$s %n");
	}
	public LogManager(String logFile) {
		try {
			fh = new FileHandler(logFile);
			SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.setProperty("java.util.logging.SimpleFormatter.format",
	              "[%1$tF %1$tT] [%4$-7s] %5$s %n");
	}
	public void log(String class_name,String message) {
		Logger log = Logger.getLogger(class_name);
		try {
			log.addHandler(fh);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.log(Level.INFO,"ID:"+count+" "+message);
		count+=1;
	}
	public void critical(String class_name,String message) {
		Logger log = Logger.getLogger(class_name);
		try {
			log.addHandler(fh);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.log(Level.SEVERE,"ID:"+count+" "+message);
		count+=1;
	}
	public int getLogCount() {
		return count;
	}
}
