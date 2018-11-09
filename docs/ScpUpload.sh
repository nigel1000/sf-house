#!/bin/sh

scp -P $4 $1 $3:$2

# ScpUpload.exp localFile remoteFile remoteHost remotePort
# ScpUpload.exp /Home/demo.jar /root/demo.jar root@127.0.0.1 22

