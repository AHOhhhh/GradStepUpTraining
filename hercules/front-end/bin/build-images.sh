#!/usr/bin/env bash

set -e

if [ -z ${GO_PIPELINE_COUNTER} ]; then
    echo "missing GO_PIPELINE_COUNTER, don't try this cmd outside CI"
    exit 1
fi

REGISTRY="nexus.hlp.fun:5001"
PACKAGE_NAME="front-end"

IMAGE_NAME="$REGISTRY/hlp/$PACKAGE_NAME"
IMAGE_TAG="$GO_PIPELINE_COUNTER"

rm -rf node_modules/

sudo docker login --username admin --password admin123 $REGISTRY
sudo docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:latest . -f Dockerfile.nopack
sudo docker push ${IMAGE_NAME}:${IMAGE_TAG}
sudo docker push ${IMAGE_NAME}:latest
