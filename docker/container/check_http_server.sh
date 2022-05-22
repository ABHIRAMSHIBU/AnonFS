#!/usr/bin/env bash
fuser 8080/tcp  2> /dev/null > /dev/null
if [ $? -ne 0 ]
then
	echo "PHP/Python/Apache Server might not be running, please check if you have started it. It must serve AnonFS.jar in the root path of the server"
	exit -1
else
	echo "HTTP Server is running"
	exit 0
fi
