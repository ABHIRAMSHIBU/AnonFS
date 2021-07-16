#!/usr/bin/env bash
echo "Creating networking infrastructure"
./network/create.sh
echo "Downloading and installing latest image"
./image/getlatest.sh
echo "Creating containers"
./container/create.sh
echo "Done.. AnonFS testnet is ready togo"

#TODO: Check if docker containers exist and does even docker service exist and is started
