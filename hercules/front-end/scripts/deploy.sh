#!/bin/bash -e

SERVICE=${1:?no service specified}
VERSION=${2:?no version specified}

cat scripts/docker-compose.yml | sed "s/@@SERVICE@@/$SERVICE/g; s/@@VERSION@@/$VERSION/g;" > docker-compose.yml

docker login -uadmin -padmin123 nexus-release.hercules.fun
docker stack deploy hercules -c docker-compose.yml --with-registry-auth
