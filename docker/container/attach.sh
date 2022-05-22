MAXPEERS=4
MINPEERS=2
if [ $# -ne 1 ]
then
	echo "Not enough or too many arguments"
	echo "./<script>.sh node_id"
	exit -1
fi
if [ $1 -lt $MINPEERS ] || [ $1 -gt $MAXPEERS ]
then
	echo "Arguemnt out of range"
	echo "It should be between $MINPEERS-$MAXPEERS"
else
	nodeid=$1
	echo Attaching to node anonfs_node$nodeid. To detach use Ctrl+p Ctrl+q
	sudo docker attach anonfs_node$nodeid
fi
