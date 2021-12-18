# Algorithms

## UploadFile - 

Input- File, List of Peers

```python
1. peers = FindSuitablePeers(connectedPeers)
2. pieces,FileMetaData = File2Pieces(file)
3. save2disk(FileMetaData)
4. DistributePieces(peers, pieces)
```



## DownloadFile - 

Input- FileMetaData, List of Peers

```python
1. for piece in FileMetaData
2. peer = FindProbablePeer(piece,peers)
3. pieceDataList.append(GetPiece(peer,piece))
4. endFor
5. Sort(pieceDataList)
6. file = Pieces2File(pieceDataList,FileMetaData)
7. return file
```





## Find Suitable Peers -

Input - Peers (location, bandwidth, ping)

```python
1. sort_lowest_euclidean_distance(Peers)
2. sort_lowest_ping(Peers)
3. ranked_peers = slice(Peers,0,50)
4. sort_highest_bandwidth(ranked_peers)
5. return ranked_peers
```







## Find Probable Peers -

Input - Peers

```python
1. ranked_peers = FindSuitablePeers()
2. probable_peers = getndistantpeers(ranked_peers,5)
3. return probable_peers
```



## File2Pieces -

Input- File, MaxPieceCount, MinPieceCount

```python
1. PieceSize = 4*1024*1034
2. if(size(file)/(PieceSize)<MinPieceCount) then
3. PieceSize = size(file)/(MinPieceCount+1)
4. if(PieceSize<1024) then
5. PieceSize = 1024
6. endif
7. endif
8. if(size(file)/(PieceSize)>MaxPieceCount) then
9. PieceSize = size(file)/(MaxPieceCount-1)
10. endif
11. while(not EOF(file)) do
12. piece = file.read(PieceSize)
13. key = new symkey()
14. FileMetaData.append(key,piece.id)
15. pieces.append((encrypt(piece,key),checksum(piece)))
16. endwhile
17. return pieces
```





## Pieces2File -

Input Pieces,FileMetaData

```python
1. for piece in pieces:
2. if(not verify(piece,FileMetaData[piece])) then
3. continue
4. else
5. file.write(decrypt(piece.data,FileMetaData[piece]),piece.start)
6. endif
7. return file
```



## PushPiece -

Input - Piece, PeerID

```python
1. key = PeerDataHandler.getPeerSymKey(PeerID)
2. if( fail == send(encrypt(Piece,key),PeerID) ) then
3. increaseTTL()
4. return failure
5. endif
6. return success
```



## GetPiece -

Input - PieceCheckSum

```python
 	1. key = PeerDataHandler.getPeerSymKey(PeerID)
 	2. if( failure == decrypt(get(Piece,PieceCheckSum),key,piece) ):
        return CHOOSE_DISTANT_PEER_INC_TTL
 	3. return piece
```



