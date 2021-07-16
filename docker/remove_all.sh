#!/usr/bin/env bash
cd container
echo "Removing Containers"
./delete.sh
cd ../
echo "Removing Images"
./image/delete.sh
echo "Removing networking infrastructure"
./network/delete.sh
echo "Done.. AnonFS testnet is now destroyed"

#TODO: Check if docker containers exist and does even docker service exist and is started
