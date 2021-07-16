#!/usr/bin/env bash
echo "Trying to delete all temporary files"
rm -rf "/tmp/anonfs_v1.tar.xz" "/tmp/anonfs_v1"
echo "Removing Image"
sudo docker image rm anonfs:v1
echo "Done"
