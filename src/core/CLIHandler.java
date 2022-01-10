package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import configuration.Configuration;

class ReaderThread extends Thread{
	public String buffer="";
	private boolean running = true;
	private boolean terminate = false;
	private BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
	public void terminateNow() {
		terminate=true;
	}
	public boolean isTerminated() {
		return !running;
	}
	public void run() {
		String line;
		while(!terminate) {
			try {
				line = stdin.readLine();
				if(line=="") {
					System.out.println("Exiting!!\n");
					break;
				}
				else {
					synchronized (this) {
						buffer+=line;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
			if(terminate) {
				System.out.println("Reader Thread is terminating!");
				running=false;
			}
		}
	}
}
public class CLIHandler extends Thread{
	private boolean echo = false;
	private ReaderThread readerThread;
	private String help = """
Commands-  

    list, echo, shell, info, upload, download, help, exit


Subcommands- 

list -  

    peers (--peers or –p) - Will list all known peers. 

    pieces (--pieces) - Will list all the avaiable pieces. 

echo - 

    on – Enables printing the command which got run. 

    off – Disables printing the command which got run. 

    * - Just print whatever argument it got. 

shell – Drop to system shell 

info - 

    version (--version) - Prints AnonFS version 

    uid (--uid or -u) - Prints UUID of the peer. 

upload - 

    File argument will upload and print file's UUID. 

download- 

    File UUID argument and output location to store file. 

exit – Exists the program.

 

CLI Format 

AnonFS-{UUID} # example command 
			""";
	private void spawnReader() {
		readerThread = new ReaderThread();
		readerThread.start();
	}
	private void clearBuffer() {
		synchronized (readerThread) {
			readerThread.buffer="";
		}
	}
	private String readBuffer() {
		String data;
		while(true) {
			synchronized (readerThread) {
				data = readerThread.buffer;
				readerThread.buffer = "";		
			}
			if(data == "") {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				break;
			}
		}
		return data;
	}
	private void stopReader() {
		readerThread.interrupt();
	}
	public void commandHandler(String in) {
		String output="";
		if(in.startsWith("echo")) {
			in = in.substring(4).strip();
			if(in.startsWith("on")) {
				echo=true;
				output="Enabled echo";
			}
			else if(in.startsWith("off")){
				echo=false;
				output="Disabled echo";
			}
			else {
				output=in;
			}
		}
		else if(in.startsWith("list")) {
			in = in.substring(4).strip();
			if(in.startsWith("--peers") || in.startsWith("-p")) {
				output+="IP/URL\t\tUID\n";
				LinkedList<String> peerlist = Core.pdh.getPeers();
				for(int i=0;i<peerlist.size();i++) {
					output+=peerlist.get(i)+"\t"+Core.pdh.getUID(peerlist.get(i))+"\n";
				}
			}
		}
		else if(in.startsWith("info")) {
			in = in.substring(4).strip();
			if(in.startsWith("--version")) {
				output=Configuration.version;
			}
			else if(in.startsWith("--uid") || in.startsWith("-u")) {
				output=Core.UIDHander.getUIDString();
			}
		}
		else if(in.startsWith("upload")) {
			in = in.substring(6).strip();
			Core.tradeHandler.uplink(in);
		}
		else if(in.startsWith("download")) {
			in = in.substring(8).strip();
			Core.tradeHandler.downlink(in);
		}
		else if(in.startsWith("help")) {
			output=help;
		}
		if(!output.equals(""))
			System.out.println(output);
	}
	public void run() {
		System.out.println("Welcome to AnonFS Integrated CLI");
		System.out.println("Warning: nonAPI communication channel");
		spawnReader();
		clearBuffer();
		while(true) {
//			System.out.println("Reading Buffer\n");
			System.out.print("AnonFS-"+Core.UIDHander.getUIDString()+" # ");
			String data = readBuffer();
//			System.out.println("Done Reading Buffer\n");
			if(echo)
				System.out.println("Command:"+data);
			if(data.toLowerCase().equals("exit")) {
				stopReader();
				System.out.println("Exiting\n");
				System.exit(0);
				break;
			}
			else if(data.toLowerCase().equals("shell")) {
				List<String> command = new LinkedList<String>();
				command.add("sh");
				ProcessBuilder pb = new ProcessBuilder(command);
				pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
				pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
				pb.redirectError(ProcessBuilder.Redirect.INHERIT);
				try {
					stopReader();
					Process sh = pb.start();
					while(sh.isAlive()) {
						Thread.sleep(100);
					}
					if(readerThread.isTerminated()==false) {
						System.out.println("Warning reader is not terminated!");
					}
					spawnReader();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				commandHandler(data);
			}
		}
	}
}
