#!/bin/sh

chmod 755 $1

appName="webApp"

cat ${appName}.pid | xargs echo kill pid is
cat ${appName}.pid | xargs kill

ulimit -n 65536
#等待服务关闭
sleep 5
#重定向日志
exec >>${appName}.stdout.log 2>>${appName}.stderr.log
#启动服务
nohup java -jar -Xms1024m -Xmx1024m -Xss256m $1 --spring.profile.active=default &
#导出进程ID
echo $! > ${appName}.pid



