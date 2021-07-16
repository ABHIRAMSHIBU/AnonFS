#!/usr/bin/env bash
echo "Trying to stop"
./stop.sh
echo "Deleting anonfs_node2"
sudo docker container rm anonfs_node2
echo "Deleting anonfs_node3"
sudo docker container rm anonfs_node3
echo "Deleting anonfs_node4"
sudo docker container rm anonfs_node4
echo "Done Deleting"
