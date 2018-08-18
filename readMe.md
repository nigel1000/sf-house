##Maven
###升级版本
```
mvn versions:set -DnewVersion=2.0.0-SNAPSHOT    #升级父项目以及所有子项目的版本
mvn versions:update-child-modules   #修改所有被依赖的版本
mvn release:prepare #切换为正式版本
mvn release:perform #发布正式版本
```