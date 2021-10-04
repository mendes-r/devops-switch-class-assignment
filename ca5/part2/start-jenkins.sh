#!/bin/zsh

CONTAINER_NAME=jenkins-container
LOG_NAME=jenkins-container.log

docker run -u root --rm -d -p 8080:8080 -p 50000:50000 -v $HOME/jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock --name $CONTAINER_NAME jenkinsci/blueocean
docker logs -f $CONTAINER_NAME >& $LOG_NAME
