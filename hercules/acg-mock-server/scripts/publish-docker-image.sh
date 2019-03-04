#!/bin/bash -e

docker login -uadmin -padmin123 nexus-release.hercules.fun

VERSION=${1:?no version specified}
TAG=nexus-release.hercules.fun/hercules/acg-mock-server:$VERSION
docker build --rm . --tag $TAG
docker push $TAG
docker rmi $TAG
