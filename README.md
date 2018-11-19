# 部署流程

## 上传
* 项目打成jar包
* 放在/data/website 目录下

## 运行
* 测试
>java -jar youjarname.jar --spring.profiles.active=test

* 生产
>java -jar youjarname.jar --spring.profiles.active=pro
