# Monthly Report for Month July 2021

Latest Commit -> b715b310011c9fc37794d18acc0253311d347524 (TCP: Added ID creation) [ date - 05-08-2021 ]

## Goals and basic debriefing 

Goals set- 

1. Networking - Add a packet based handling scheme.
2. Autoconnection - Bringup bootstrapping and automatic peer reconnection.
3. FileToPiece - Bringup file to piece.
4. Encryption - Bring up piece encryption and identity management
5. Create A Sate Diagram

Goals attained-

1. Peer Handling - Find and add report disconnected and connected peers
2. Networking - Added a packet based peer communication handler
3. Autoconnection - Brought up autoreconnection module. 
4. State diagram created.

## Diagrams

![image-20210806180402259](/home/abhiram/eclipse-workspace/AnonFS/images/NFA_AnonFS)



# Monthly Report for Month Jun 2021

Latest Commit - > 2d5106d3ae7dc17b8562b1e39f70b1eab2c7eeba (TCP: Handler should now be separated, socket is now bytes)[ date - 03/07/2021 ]

## Goals and basic debriefing 

Goals set- 

1. Networking - Establish TCP connection between nodes.
2. Explore Azure - Install VM and check feasibility of Azure platform
3. Establish Directory Service - Allow nodes to discover other nodes via directory servers
4. Dummy modules - Create Dummy modules for a top down approach
5. Setup IDE and proper tools - Best IDE which will allow easy debugging needs to be set.

Goals attained (including additional) -

1. Networking
2. Explore Azure
3. Establish Directory Service (Partial)
4. Setup IDE and proper tools.
5. Docker- Create Docker Image for easy deployment
6. RSA Encryption - Add classes for future use for identity management (Host, Client, User Pending)
7. Configuration Management - Create a configuration file and parser (using ini4j as parser).
8. Log Management - Created a class for efficient log creation and management with levels of criticality.
9. Peer Management - Created a class for storing and keeping peer information.
10. Tree Binary - Hashmap shortcut tree logic for faster lookup on a hashmap (may be useless in future). Used for storing piece information on disk and efficiently finding piece on disk.

## Commits Related to each Goals

### 1. Networking

1) [TCP: Added class but untested](https://github.com/ABHIRAMSHIBU/AnonFS/commit/7240f16b51306d55c5b85230cfffa890f532d584)

2) [TCP: Use different config path](https://github.com/ABHIRAMSHIBU/AnonFS/commit/f75da0d6187b70ea7b44cbf600b48a6086b830c3)

3) [TCP: Cleanup of source code](https://github.com/ABHIRAMSHIBU/AnonFS/commit/cc774f90c9941afd90efe616d89b7e19398e2df7)

4) [TCP: Added base response for server](https://github.com/ABHIRAMSHIBU/AnonFS/commit/3ef7c29d2c3455da3f0d4ca6d8904551c1e5b214)

5) [Core: Now TCP Server will start with application](https://github.com/ABHIRAMSHIBU/AnonFS/commit/3789ac38e2261f631c002d7f4febf5e1c0313c1f)

6) [TCPServer: Adjust spacing](https://github.com/ABHIRAMSHIBU/AnonFS/commit/aaf1fbb914eaae1cbd327a2ac698966a04a2989f)

7) [TCP: Handler should now be seperated, socket is now bytes](https://github.com/ABHIRAMSHIBU/AnonFS/commit/2d5106d3ae7dc17b8562b1e39f70b1eab2c7eeba)

### 3. Establish Directory Service (Partial)

1) [TCPServer: Added GetPiece and FindPiece prototype, and MyIP Implement…](https://github.com/ABHIRAMSHIBU/AnonFS/commit/338d2765f7540db49304b404d0c0816db3c49355)               

### 4. Setup IDE and proper tools

1) [Add gitignore](https://github.com/ABHIRAMSHIBU/AnonFS/commit/6fd3923287f87093962801fa1fc266a31224abcc)

2) [Move gitignore to proper place](https://github.com/ABHIRAMSHIBU/AnonFS/commit/eae2f7babd1062b4cf5eef0de4ee4c8b5b4fd391)

3) [Netbeans: Project Config](https://github.com/ABHIRAMSHIBU/AnonFS/commit/8045a94e4155b62aed11e69e3a1d026c2ad2c447)

4) [Project: Now its both eclipse as well as netbeans project](https://github.com/ABHIRAMSHIBU/AnonFS/commit/f5bd8749707035a06827f3cb362ab96068194d09)

5) [gitignore: Fixed a typo for preventing logs into git](https://github.com/ABHIRAMSHIBU/AnonFS/commit/b3260a420c206bf06f073e786b55a340cc337522)

### 6. RSA Encryption

1) [Cryptography: Added RSA HostKey and ClientKey Managment](https://github.com/ABHIRAMSHIBU/AnonFS/commit/6e91fa4dc416678f9c10cc659187016074141474)

### 7. Configuration Management

1) [Configuration: Added libs and a working config parser/creator](https://github.com/ABHIRAMSHIBU/AnonFS/commit/18e2187ac1fec3e139246343eb9f4dfa18132845)

2) [Configuration: Configuration logged properly, added version](https://github.com/ABHIRAMSHIBU/AnonFS/commit/11ba4de43ec5b17438a640ae9bc2c2fa36818498)

3) [Configuration: Added bootstrap peer](https://github.com/ABHIRAMSHIBU/AnonFS/commit/1b5d39157bf5cd766a26b6f26f021eab4bedd09b)

### 8. Log Management

1) [Logger: Added log managment](https://github.com/ABHIRAMSHIBU/AnonFS/commit/bc01fb7a51a81668b009913533119f760f1d7e17)

2) [Logger: Cleanup formatting, but now default used broken](https://github.com/ABHIRAMSHIBU/AnonFS/commit/e76bf414811ab91aff1fd47a324b2199eddc6a0c)

### 9. Peer Management

1) [PeerDataHander: Now this will be used to handle peers](https://github.com/ABHIRAMSHIBU/AnonFS/commit/c6894d0ec450f1ef0f262504cd88b9b9e79c3b09)

### 10. Tree Management

1) [Tree: Added tree datastructure](https://github.com/ABHIRAMSHIBU/AnonFS/commit/9a26710ceedbe4a1f34d4a1fc6a5af560469bcff)

2) [Tree: Moved test to a class](https://github.com/ABHIRAMSHIBU/AnonFS/commit/6431b5de82c92dac57dae698171f6ea3da7bca29)

## Architecture 

### Figures

#### Figure 1 Peer to Peer connection

![image-20210703171658848](images/Peer_to_Peer)

#### Figure 2 Sample mesh connection

![image-20210703172557671](images/Mesh_example)

#### Figure 3 Socket Connection Handling Class Perspective

![image-20210703172658253](images/Socket_handling_class_perspective)

#### Figure 4 Getting file ready to be uploaded into AnonFS

![image-20210703172816365](images/File2Piece)

#### Figure 5 Structure of a piece

![image-20210703172917446](images/Structure_of_piece)

#### Figure 6 Delivering 2 pieces to AnonFS

![image-20210703173007282](images/Delivering_2_piece)

#### Figure 7 Downloading 2 pieces from AnonFS

![image-20210703173113039](images/Downloading_of_piece)



#### Figure 8 Getting back file from the pieces using assembly information

![image-20210703173208052](images/Getting_file_back_from_piece)

#### Figure 9 Assembly Information Structure

![image-20210703173253957](images/Assembly_db_structure)



### Connecting to Peer

Connection to peer is done via TCP. A single socket connection is made by a peer to another peer. All peers with public IP address will be having a TCPServer at port 9090. When a peer for example lets call it B connects to another peer with public IP address lets say A, then there will exist 3 phases Figure 1

1. Peer A by default is listening on port 9090

2. Peer B does a connect primitive to connect to Peer A

3. Peer A does an accept primitive to accept the connection from Peer B

Now Peer A has a new random port apart from 9090 connected to Peer B and Peer B uses the socket it connected with to Peer A.

This can be visualized as Figure 3 in implementation. Here TCP Server class is what gets the request of connection from Peer B and does the accept primitive to accept the connection there by creating a new random port on Peer A and sending the socket object associated with the new port to TCP Hander. Connection initiation is always done by the TCP Hander itself so it itself creates the socket object in the Peer B. Both transfers all the details to Core class as it's the glue that keep together all the classes and co-ordinates the actions.

When peers are connected together with a max simultaneous connection limit set to 2, Figure 2 can be taken as an example of such a mesh network. We have limited the number of connections so peers connect to 2 of the peers that they feel are closer to them.

### Uploading File to AnonFS

When a file needs to be uploaded into AnonFS, it needs to be divided into pieces. Piece size is decided by the configuration. If the piece size is smaller then it will result in the file being more distributed, but this also means that there is a chance for reduced speeds overall. So its necessary to keep a balance between distribution and speed. More number of piece will also mean that there will be more data lost just for keeping the header of each piece. Conversion of a file to piece and the Assembly information (Figure 9) is given in Figure 4. Structure of how a piece looks is given in Figure 5. Will_To_Live is basically how long after last access, piece should be alive.

When a piece is uploaded into a peer, there is a parameter that is to be specified with it, that is the TTL. TTL like in network is basically the time for the request to exist in the network. This can be used for distributing using peers to other peers or even used for preventing request flooding problem. In Figure 6, Peer 1 sends Piece 1 and Piece 2 to both Peer 2 and Peer 3 with a TTL of 1. This is why they relay the Piece to another neighbor which is not the originator of the request. 

### Downloading File from AnonFS

Downloading file is basically the above steps in reverse, now we will ask the nodes which we send the data to return the data. This message can also have a TTL which can be used to mirror the data to other nodes if necessary also to limit the propagation of request to under the TTL value. In Figure 7 Piece 1 gets a timeout when requested from Peer 2 this may be because Peer 2 was slow or was offline.

Figure 8 shows how the pieces is getting assembled to the original file. When it is assembled, checksum and signature are verified, if found faulty, its requested from another peer, and once it gets the valid piece it will notify other peer with correct information. For the original file to be revived, the peer must have its assembly information which is not kept in AnonFS and is supposed to be carried by the person who owns the file.