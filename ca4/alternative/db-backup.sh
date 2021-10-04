#!/bin/bash

ssh root@104.248.245.36 <<EOF
docker start 937ad8c11a0e
cd ~/ca4-devops-alt
docker exec 937ad8c11a0e /bin/sh -c 'cp *.db ../data/'
exit
EOF
sftp root@104.248.245.36 <<EOF 
get ca4-devops-alt/data/*.db ./data 
exit
EOF
