#!/bin/sh

docker run -d --name hna_front_end --restart=always -p 1337:1337 nexus.hlp.fun:5001/hlp/front-end:latest