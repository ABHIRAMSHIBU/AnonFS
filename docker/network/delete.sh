#!/usr/bin/env bash
echo "Deleting anonfs-network"
sudo docker network rm anonfs-network
echo "Done"
