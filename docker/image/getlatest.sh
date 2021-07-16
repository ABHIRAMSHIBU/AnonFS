#!/usr/bin/env bash
echo "Downloading latest image to /tmp"
aria2c -x10 -d /tmp https://abhiramshibu.tuxforums.com/~abhiram/anonfs/images/anonfs_v1.tar.xz
echo "Download complete"
echo "Extracting"
cd /tmp
tar -xvf anonfs_v1.tar.xz
echo "Extract Success"
echo "Importing Docker Image"
sudo docker import anonfs_v1 anonfs:v1
if [ $? -eq "0" ]; then
	echo "Importing Success"
else
	echo "Importing Failure"
fi
echo "Cleanup"
rm -rf /tmp/anonfs_v1.tar.gz anonfs_v1
echo "Done"
