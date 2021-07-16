#!/usr/bin/env bash
echo "Stopping anonfs_node2"
sudo docker container stop anonfs_node2
echo "Stopping anonfs_node3"
sudo docker container stop anonfs_node3
echo "Stopping anonfs_node4"
sudo docker container stop anonfs_node4
echo "Done stoping.."
