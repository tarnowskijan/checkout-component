#!/bin/bash
set -e
mvn clean package
VERSION=`ls ./target/ | grep checkout-component-.*.jar | sed 's/checkout-component-\(.*\)\.jar.*/\1/g' | head -n 1`
COMMIT=`git log -n 1 --pretty=format:"%H"`
AWS_ECR_TAG=666680944018.dkr.ecr.eu-central-1.amazonaws.com/checkout-component
docker build -t $AWS_ECR_TAG:$VERSION -t $AWS_ECR_TAG:$COMMIT -t $AWS_ECR_TAG:latest .