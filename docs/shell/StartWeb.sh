#!/bin/sh

sshHost="hznijianfeng@ip"
sshPort="1046"
passwd=""
sshRootPath="/home/nijianfeng/webApp/"

shPath="/Users/nijianfeng/Documents/projects/sf-house/docs/"
jarPath="/Users/nijianfeng/Documents/projects/sf-house/sf-house-project-web/target/"
jarName="sf-house-project-web-1.0.jar"
remoteJarPath="${sshRootPath}/${jarName}"
echo upload jarPath is: ${jarPath}/${jarName}

chmod 755 ${shPath}/ScpUpload.sh
chmod 755 ${shPath}/SshRun.sh
chmod 755 ${shPath}/shell/WebRunJar.sh

# 构建项目
cd ${jarPath}
cd ..
mvn clean install

# 免输密码进行文件传输
${shPath}/ScpUpload.sh ${jarPath}/${jarName} ${remoteJarPath} ${sshHost} ${sshPort} ${passwd}
${shPath}/ScpUpload.sh ${shPath}/shell/WebRunJar.sh ${sshRootPath}/WebRunJar.sh ${sshHost} ${sshPort} ${passwd}

# 免输密码进行命令执行
${shPath}/SshRun.sh ${sshHost} ${sshPort} "sh ${sshRootPath}/WebRunJar.sh ${remoteJarPath}"


# sh /Users/nijianfeng/Documents/projects/sf-house/docs/shell/StartWeb.sh