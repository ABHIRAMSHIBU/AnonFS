package core;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import algorithm.ByteArrayTransforms;
import algorithm.FileTransforms;
import connectivity.TCPHander;
import datastructures.CallBackPromise;
import datastructures.MetaDataHandler;
import datastructures.Packet;
import datastructures.PeerDataHandler;
import datastructures.Piece;

public class TradeHandler {
	public String uplink(String filename) {
		Core.logManager.log(TradeHandler.class.getName(), "uplink: "+filename);
		// TODO: Check if file exists
		// Send the file
		LinkedList<String> peers ;
//		synchronized (Core.pdh) {
			peers = Core.pdh.getPeers();
//		}
		// Bail out if no peers
		if(peers.size()==0) {
			Core.logManager.log(this.getClass().getName(), "Not sufficient amount of peers! BAIL OUT");
			System.out.println("Not enough peers to upload");
			return null;
		}
		
		// Get the metadata and create pieces
		MetaDataHandler mdh = new MetaDataHandler();
		LinkedList<Piece> pieces = FileTransforms.FileToPices(filename, Core.config.getMaxPieceSize() , mdh);
		
		try {
			Core.logManager.log(this.getClass().getName(), "Saving metadata to Disk");
			Core.metadataDiskStorage.metadataToDisk(mdh);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Write the pieces to disk
		HashMap<String,Boolean> pieceMap = null;
		try {
			Core.logManager.log(this.getClass().getName(), "Saving pieces to Disk");
			 pieceMap = Core.pieceDiskStorage.piecesToDisk(pieces);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Choose a peer to send file to
		LinkedList<String> chosen_peer_list= new LinkedList<String>();
		for(int i=0;i<peers.size();i++) {
			String chosen_peer = peers.get(i);
			synchronized (Core.pdh) {
				if(Core.pdh.getConnected(chosen_peer) && (Core.pdh.getSelfHost(chosen_peer)==0)) {
					// At this point we have found a good peer add it to list.
					chosen_peer_list.add(chosen_peer);
				}
			}
		}
		Core.logManager.log(this.getClass().getName(), "Peer Chosen: "+chosen_peer_list.get(0));
		
		for(int i=0;i<pieces.size();i++) {
			// At this point we have piece in our hand, we have to just send it..
			Piece piece = pieces.get(i);
			try {
				String checksum = ByteArrayTransforms.toHexString(piece.getChecksum());
				if(!(pieceMap.containsKey(checksum) && pieceMap.get(checksum))) {
					Core.logManager.log(this.getClass().getName(),"BLUE BLUE BLUE");
					// Assuming others have the piece, because it is in the disk
					continue;
				}
			}
			catch(Exception e) {
				Core.logManager.log(this.getClass().getName(), e.toString());
				e.printStackTrace();
			}
			Core.logManager.log(this.getClass().getName(),"BLAH BLAH BLAH");
			PeerDataHandler pdh = Core.pdh;
			
			TCPHander handler;
			Queue<Object> qcallback;
			SocketChannel socketChannel;
			int selfHost;
			synchronized (pdh) {
				handler = pdh.getTCPHander(chosen_peer_list.get(0));
				qcallback = pdh.getReplyQueue(chosen_peer_list.get(0));
				socketChannel = pdh.getOutputStream(chosen_peer_list.get(0));
				selfHost = pdh.getSelfHost(chosen_peer_list.get(0));
			}
//			System.out.println("SEND:"+piece.toString()+"Length:"+piece.toString().length());
			byte [] temp = ByteArrayTransforms.toByteArray("PushPiece"+piece.toString());
//			System.out.println(temp.length);
			HashMap<String, Object> packetWrapper = handler.p.createPacket(temp,
											   					           Packet.REQUEST,
					 													   0,
					 													   (byte)0);
//			handler.p.decodePacket((byte [])packetWrapper.get("packet"));
//			System.out.println("Decoded Size"+handler.p.getDecodedSize());
			byte packet[] = (byte[]) packetWrapper.get("packet");
			byte packetid = (byte) packetWrapper.get("id");
			CallBackPromise cbp = new CallBackPromise(packetid);
			
			synchronized (cbp) {
				ByteBuffer buffer = ByteBuffer.allocate(packet.length);
				buffer.put(packet);
				buffer.flip();
				try {
					qcallback.add(cbp);
					while(buffer.hasRemaining()) {
						socketChannel.write(buffer);
					}
					Core.logManager.log(this.getClass().getName(), "Going to wait on Call Back Promise ID:"+handler.p.getCreatedID(),4);
					cbp.wait();
					Core.logManager.log(this.getClass().getName(), "Wait finished on Call Back Promise",4);
					
				} catch (SocketException e) {
					synchronized (pdh) {
						pdh.setConnected(chosen_peer_list.get(0), false);
					}
				}
				catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			byte recievedData[] = cbp.data;
			if(recievedData!=null) {
				System.out.println(new String(recievedData));
			}
			else {
				System.out.println("Didnt recieve any data!");
			}
		}
		String md5sum_mdh = ByteArrayTransforms.toHexString(mdh.getChecksum());
		System.out.println("File for "+filename+" is ID: "+md5sum_mdh);
		return md5sum_mdh;
	}
	public void downlink(String metadataID, String filename) {
		// TODO: Check if Metadata exists
		System.out.println("MetadataID:"+metadataID);
		// Download the file
		LinkedList<String> peers = Core.pdh.getPeers();
		// Bail out if no peers
		if(peers.size()==0) {
			Core.logManager.log(this.getClass().getName(), "Not sufficient amount of peers! BAIL OUT");
			System.out.println("Not enough peers to download");
			return;
		}
		
		// Get Metadata from disk
		MetaDataHandler mdh = null;
		try {
			mdh = Core.metadataDiskStorage.diskToMetadata(metadataID);
			for(long i=0;i<mdh.size();i++) {
				byte [] piecechecksum = mdh.getEntry(i);
				
				// Choose a peer to get file from
				LinkedList<String> chosen_peer_list= new LinkedList<String>();
				for(int j=0;j<peers.size();j++) {
					String chosen_peer = peers.get(j);
					synchronized (Core.pdh) {
						if(Core.pdh.getConnected(chosen_peer) && (Core.pdh.getSelfHost(chosen_peer)==0)) {
							// At this point we have found a good peer add it to list.
							chosen_peer_list.add(chosen_peer);
						}
					}
				}
				
				
				
				PeerDataHandler pdh = Core.pdh;
				
				TCPHander handler;
				Queue<Object> qcallback;
				SocketChannel socketChannel;
				
				int selfHost;
				synchronized (pdh) {
					handler = pdh.getTCPHander(chosen_peer_list.get(0));
					qcallback = pdh.getReplyQueue(chosen_peer_list.get(0));
					socketChannel = pdh.getOutputStream(chosen_peer_list.get(0));
					selfHost = pdh.getSelfHost(chosen_peer_list.get(0));
				}
				System.out.println("Piece Request:"+ByteArrayTransforms.toHexString(piecechecksum));
				byte [] temp = ByteArrayTransforms.toByteArray("GetPiece"+ByteArrayTransforms.toHexString(piecechecksum));
				
				
				HashMap<String, Object> packetWrapper = handler.p.createPacket(temp,
											            Packet.REQUEST,
												 	    0,
													    (byte)0);
				
				
				byte packet[] = (byte[]) packetWrapper.get("packet");
				byte packetid = (byte) packetWrapper.get("id");
				CallBackPromise cbp = new CallBackPromise(packetid);
				
				
				synchronized (cbp) {
					try {
						qcallback.add(cbp);
						ByteBuffer buffer = ByteBuffer.allocate(packet.length);
						buffer.put(packet);
						buffer.flip();
						synchronized (socketChannel) {
							while(buffer.hasRemaining()) {
								socketChannel.write(buffer);
							}
						}
						Core.logManager.log(this.getClass().getName(), "Going to wait on Call Back Promise ID:"+handler.p.getCreatedID(),4);
						cbp.wait();
						Core.logManager.log(this.getClass().getName(), "Wait finished on Call Back Promise",4);
						
					} catch (SocketException e) {
						synchronized (pdh) {
							pdh.setConnected(chosen_peer_list.get(0), false);
						}
					}
					catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				byte recievedData[] = cbp.data;
				if(recievedData!=null) {
					System.out.println(new String(recievedData));
					if(new String(recievedData).startsWith("PDNE!")){
						System.out.println("Error: Piece Do Not Exist! Abort");
						return;
					}
					Piece p = new Piece();
					p.fromString(new String(recievedData));
					Core.pieceDiskStorage.pieceToDisk(p);
				}

			}
			// From disk create the file.
			LinkedList<Piece> pieces = Core.pieceDiskStorage.getPiecesWithMetaData(mdh);
			FileTransforms.PiecesToFile(filename, pieces);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
