#!/bin/sh

root_path=`pwd`

cd game/tools

logback_path="${root_path}""/game/resource/logback.xml"

bilog_path=`pwd`
bilog_path="${root_path}""/log"

java -cp ./packet_helper.jar com.kodgames.main.UpdateGameBilogConfig $logback_path $bilog_path

dbUser=`head -1 db.prop | tr -d "\r"`
dbPwd=`head -2 db.prop | tail -1 | tr -d "\r"`

echo $dbUser
echo $dbPwd

u="-u""${dbUser}" 
p="-p""${dbPwd}"

mysql $u $p < create_game.sql
mysql $u $p < create_auth.sql

cd $root_path
