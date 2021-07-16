#!/usr/bin/env bash
echo "Starting anonfs_node2"
sudo docker container start anonfs_node2
echo "Starting anonfs_node3"
sudo docker container start anonfs_node3
echo "Starting anonfs_node4"
sudo docker container start anonfs_node4
echo "Done starting.. do attach to interact"
