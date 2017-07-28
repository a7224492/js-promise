1、配置文件在resource目录下详细介绍如下（端口默认为3306没有提成配置）：
remotehost=115.159.126.127		远程服务器ip（数据在这里）
localhost=127.0.0.1				本地服务器ip（数据导入到这里）
remoteuser=erdosmj_write		远程服务器数据库用户名
remotepassword=4mZ281FsBz		远程服务器数据库密码
localuser=root					本地数据库用户名
localpassword=root				本地数据库密码
dbname=replay					不用动，默认使用replay服务器
双击mysql2zdb.bat，需要安装java
1、先输入玩家的roleId，然后输入登录用户名，（生成一个玩家，roleId相同）
2、在输入一遍玩家的roleId（这一步骤可以循环，是在导战绩信息）

注意：
1、输入的登录用户名为在localhost上登录的用户名，test渠道
2、完成后需要重启auth服务器，否则玩家的roleId不会变（如果输入的用户名为新的可以不用重启auth）
3、战绩需要拆分为replay服务器