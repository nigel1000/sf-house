

basedir=`cd $(dirname $0); pwd -P`
echo $basedir
cd $basedir

# 修改版本
mvn versions:set -DnewVersion=404.0.0-SNAPSHOT

# 若修改失败,可是使用命令回退版本号:
# mvn versions:revert
# 若确认版本，可使用命令:
mvn versions:commit

# 发布 jar
# -am 表示同时处理选定模块所依赖的模块
# -pl 选项后可跟随{groupId}:{artifactId}或者所选模块的相对路径(多个模块以逗号分隔)
# -amd 表示同时处理依赖选定模块的模块
# -N 表示不递归子模块
mvn clean install -Dmaven.test.skip=true -am -pl sf-house-bean,sf-house-aop

