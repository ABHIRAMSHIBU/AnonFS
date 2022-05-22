#!/usr/bin/env bash
./check_http_server.sh
if [ 0 -ne $? ]
then
	echo "HTTP Error cannot proceed"
	exit -1
fi
echo "Starting anonfs_node2"
sudo docker container start anonfs_node2
echo "Starting anonfs_node3"
sudo docker container start anonfs_node3
echo "Starting anonfs_node4"
sudo docker container start anonfs_node4
echo "Done starting.. do attach to interact"
