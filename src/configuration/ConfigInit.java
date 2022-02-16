package configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.ini4j.Ini;

import core.Core;
public class ConfigInit {
	PrintWriter pw ;
	File configFile = new File("AnonFSd.conf");
	Ini ini;
	public boolean failure = true;
	public ConfigInit() throws IOException{
		/**Its supposed to initialize the configuration for basic configuration
		 * */
		if(!configFile.exists()) {
			pw = new PrintWriter(new FileWriter(configFile));
			Core.logManager.log(this.getClass().getName(), "Configuration file does not exist, creating.");
			pw.print("# AnonFS v0.1 Configuration \n"
					+ "\n"
					+ "[network]\n"
					+ "# MAX_UPLOAD_SIZE when equal to 0 will be unlimited which is the default \n"
					+ "# when it is undeclared. When you set max upload size, your data will be broken \n"
					+ "# down into pieces smaller than this. It will also prevent downloading pieces which \n"
					+ "# are bigger than this if asked by another peer, that means it wont be able to relay \n"
					+ "# piece of higher size. Setting it to less than 4MB is not worth it. \n"
					+ "# To set maximum upload size, uncomment the line below \n"
					+ "\n"
					+ "# MAX_UPLOAD_SIZE=32M \n"
					+ "\n"
					+ "# DAILY_QUOTA_DOWNLOAD when equal to 0 will be unlimited which is the default \n"
					+ "# when it is undeclared. When you set daily quota download, it will reduce internet \n"
					+ "# use or even suspend from operation such that daily download does not exceed  \n"
					+ "# your limit. After this quota is exceeded your node will not be functional. \n"
					+ "# To set daily download limit, uncomment the line below \n"
					+ "\n"
					+ "# DAILY_QUOTA_DOWNLOAD = 1G \n"
					+ "\n"
					+ "# DAILY_QUOTA_UPLOAD when equal to 0 will be unlimited which is the default \n"
					+ "# when it is undeclared. When you set daily quota upload, it will reduce internet \n"
					+ "# use or even suspend from operation such that daily upload does not exceed  \n"
					+ "# your limit. After this quota is exceeded your node will not be functional. \n"
					+ "# To set daily download limit, uncomment the line below \n"
					+ "\n"
					+ "# DAILY_QUOTA_UPLOAD=1G  \n"
					+ "\n"
					+ "# RELAY_ENABLE when set to false will forbid your client from downloading \n"
					+ "# or uploading pieces which you do not have keys to.  It will not technically forbid \n"
					+ "# rather your client will choose not to save or relay requests. It will also tell other \n"
					+ "# peers not to ask for data. \n"
					+ "\n"
					+ "# RELAY_ENABLE=false \n"
					+ "\n"
					+ "# MAX_CONNECTIONS basically dictates how much concurrent connections  \n"
					+ "# you can afford. Increasing this value will create stress on your router and  \n"
					+ "# network. If you have any limits on number of ports being opened, this \n"
					+ "# might use up the ports. By default it is unlimited but restricted to nodes \n"
					+ "# with lowest pings. This will be a hard limit when set and will always try to  \n"
					+ "# keep that many number of connections set even if pings are worse. \n"
					+ "# To override you can uncomment the line below. \n"
					+ "\n"
					+ "# MAX_CONNECTIONS=10 \n"
					+ "\n"
					+ "# DIRECTORY_LIST when set to true will show your willingness to participate \n"
					+ "# as this service currently has less nodes available, this is default enabled. \n"
					+ "# But if you ever wish to disable it, you can set it to false. When directory listing \n"
					+ "# you might get additional network usage. \n"
					+ "\n"
					+ "# DIRECTORY_LIST=false \n"
					+ " \n"
					+ "# ADVERTISE_ENABLE will make you willing to accept more traffic, when this is \n"
					+ "# enabled you will become one of the main directory listing server. This option \n"
					+ "# is completely ignored if DIRECTORY_LIST is set to false. By default it is enabled \n"
					+ "# due to low peer count. \n"
					+ "\n"
					+ "# ADVERTISE_ENABLE=false \n"
					+ "\n"
					+ "# TOR is used to secure the communication even further, enabling it might slow down \n"
					+ "# the whole network but might give extra layer of security. If you enable tor, it will use \n"
					+ "# tor socks proxy with port 9050 this means you need to have tor already running. \n"
					+ "# To enable tor uncomment the below line \n"
					+ "\n"
					+ "# TOR_ENABLE=true \n"
					+ "\n"
					+ "# FLOOD_ENABLE basically makes you known by many peers as possible to increase  \n"
					+ "# chance of close proximity to the data. Enabling this might increase network traffic  \n"
					+ "# hence it is discouraged to enable it. \n"
					+ "# To enable flood uncomment the line below. \n"
					+ "\n"
					+ "# FLOOD_ENABLE=true \n"
					+ "\n"
					+ "# MAX_PIECE_SIZE is basically used to set the size of pieces. Bigger the size is file \n"
					+ "# cannot be scattered easily leading to centralization but if you put piece size too  \n"
					+ "# low, it might take longer time to find and assemble file because it may be scattered. \n"
					+ "# To set piece size uncomment below line, by default its 4MiB \n"
					+ "\n"
					+ "# MAX_PIECE_SIZE=512 \n"
					+ "\n"
					+ "# BOOTSTRAP_PEER is the peer with which AnonFS bootstraps. This peer will be used to\n"
					+ "# know other peers. You can set it to a custom peer of your choice.\n"
					+ "\n"
					+ "BOOTSTRAP_PEER=192.168.43.143\n"
					+ "\n"
					+ "[disk]\n"
					+ "# Whenever you download or relay something, it will be stored also In your \n"
					+ "# local disk. All data floating around is encrypted so you won't have access to \n"
					+ "# real data in it. By default, the limit is 10GiB but if you need to increase it or \n"
					+ "# decrease it, you can uncomment below line     \n"
					+ "\n"
					+ "# MAX_DISK_SIZE=32G \n"
					+ "\n"
					+ "# Peer root is where the data is stored. This should be a disk with good speeds \n"
					+ "# but preferably not a SSD as SSD do have write cycles. This directory may be  \n"
					+ "# written or read from during the operation of your node. \n"
					+ "# To change the directory where pieces is written, edit the line below \n"
					+ " \n"
					+ "PEER_ROOT=/var/lib/AnonFS/ \n"
					+ "\n"
					+ "[logging]\n"
					+ "# Log level n means all logs greator than n and all log equal to n will be printed \n"
					+ ""
					+ "LOG_LEVEL=5\n"
					+ "");
			pw.close();
		}
		// Please don't use loglevel here as loglevel is not yet avaiable as of now.
		else {
			Core.logManager.log(this.getClass().getName(), "Configuration file exists, reading.");
		}
		ini = new Ini(configFile);
		Core.logManager.log(this.getClass().getName(),"Configuration Parsed-> "+ini.toString());
		failure = false;
	}
	public Ini getIni() {
		return ini;
	}
	public String getBootStrapPeer() {
		return ini.fetch("network", "BOOTSTRAP_PEER");
	}
	public int getMaxPieceSize() {
		return Integer.parseInt(ini.fetch("network", "MAX_PIECE_SIZE"));
	}
	public String getPeerRoot() {
		String root = ini.fetch("disk", "PEER_ROOT");
		if(root == null) {
			return "store";
		}
		else {
			return root;
		}
		
	}
	public int getLogLevel() {
		String logLevel = ini.fetch("logging", "LOG_LEVEL");
		if(logLevel==null) {
			return 5;
		}
		else {
			return Integer.parseInt(logLevel);
		}
	}
}
