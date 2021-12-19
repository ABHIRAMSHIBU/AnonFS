printf "Enter nodeid 2-4:"
read nodeid
echo Attaching to node anonfs_node$nodeid. To detach use Ctrl+p Ctrl+q
sudo docker attach anonfs_node$nodeid

