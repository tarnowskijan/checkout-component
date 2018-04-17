#!/bin/bash
set -e
DOCKER_LOGIN_CMD=`aws ecr get-login --no-include-email`
eval $DOCKER_LOGIN_CMD
docker push 666680944018.dkr.ecr.eu-central-1.amazonaws.com/checkout-component