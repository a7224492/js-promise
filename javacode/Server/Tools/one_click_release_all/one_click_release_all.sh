#!/bin/sh
cd ..
svn revert ../jar/*.jar
echo $?
# one_click_release_all/svn_cmd/svn.exe info ../GameServer/src > ../GameServer/src/build_src.txt
# one_click_release_all/svn_cmd/svn.exe info ../CorgiServerCore/src > ../CorgiServerCore/src/build_src.txt
# one_click_release_all/svn_cmd/svn.exe info ../Message/src > ../Message/src/build_src.txt
# one_click_release_all/svn_cmd/svn.exe info ../BattleServer/src > ../BattleServer/src/build_src.txt
# one_click_release_all/svn_cmd/svn.exe info ../GatewayServer/src > ../GatewayServer/src/build_src.txt

echo -------------Message.jar---------------------------
cd ..
cd ./Message
ant -buildfile ./build_no_api.xml default
echo $?
if [ $? -ne 0 ];then
	echo compile error
 	exit 0
fi

echo -------------CorgiServerCore.jar---------------------------
cd ..
cd ./CorgiServerCore
ant -buildfile ./build_no_api.xml default
if [ $? -ne 0 ];then
	echo compile error
	exit 0
fi

echo -------------ManageServer.jar---------------------------
cd ..
cd ./ManageServer
ant -buildfile ./build_no_api.xml default
if [ $? -ne 0 ];then
	echo compile error
	exit 0
fi

echo -------------InterfaceServer.jar---------------------------
cd ..
cd ./InterfaceServer
ant -buildfile ./build_no_api.xml default
if [ $? -ne 0 ];then
	echo compile error
	exit 0
fi

echo -------------AuthServer.jar---------------------------
cd ..
cd ./AuthServer
ant -buildfile ./build_no_api.xml default
if [ $? -ne 0 ];then
	echo compile error
	exit 0
fi

echo -------------GameServer.jar---------------------------
cd ..
cd ./GameServer
ant -buildfile ./build_no_api.xml default
if [ $? -ne 0 ];then
	echo compile error
	exit 0
fi

echo -------------BattleServer.jar---------------------------
cd ..
cd ./BattleServer
ant -buildfile ./build_no_api.xml default
if [ $? -ne 0 ];then
	echo compile error
	exit 0
fi

echo -----------GatewayServer.jar--------------------------
cd ..
cd ./GatewayServer
ant -buildfile ./build_no_api.xml default
if [ $? -ne 0 ];then
	echo compile error
	exit 0
fi

echo -----Success !!!-----------