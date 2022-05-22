#!/usr/bin/env bash
echo "Docker creating container anonfs_node2"
sudo docker container run -d --name anonfs_node2 --net anonfs-network --ip 192.168.1.2 -it anonfs:v1 /bin/bash -c  "cd /root; ./dar.sh"
echo "Docker creating container anonfs_node3"
sudo docker container run -d --name anonfs_node3 --net anonfs-network --ip 192.168.1.3 -it anonfs:v1 /bin/bash -c "cd /root; ./dar.sh"
echo "Docker creating container anonfs_node4"
sudo docker container run -d --name anonfs_node4 --net anonfs-network --ip 192.168.1.4 -it anonfs:v1 /bin/bash -c "cd /root; ./dar.sh"
echo "Done creating containers"
