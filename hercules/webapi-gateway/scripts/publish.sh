#!/bin/bash -e


SERVICE=${1:?no service specified}
VERSION=${2:?no version specified}
TAG=nexus-release.hercules.fun/hercules/$SERVICE:$VERSION

cat scripts/Dockerfile | sed "s/@@SERVICE@@/$SERVICE/g; s/@@VERSION@@/$VERSION/g;" > Dockerfile

docker login -uadmin -padmin123 nexus-release.hercules.fun
docker build --rm . --tag $TAG
docker push $TAG
docker rmi $TAG
