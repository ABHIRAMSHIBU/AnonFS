package core;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import connectivity.TCPClient;
import logging.LogManager;

// Dummy core 
public class Core {
	public static LogManager logManager;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			logManager = new LogManager();
			System.out.print("Enter an ip to connect to:");
			String ip = in.readLine();
			TCPClient tcpc = new TCPClient(ip);
			while(true) {
				System.out.print("Enter an input:");
				String input = in.readLine();
				if(input.compareTo("exit")==0) {
					break;
				}
				else {
					tcpc.sendRequest(input);
					System.out.println("Send Request");
				}
			}
		}
		catch(Exception e){
			logManager.log(Core.class.getName(), e.toString());
		}
	}

}
