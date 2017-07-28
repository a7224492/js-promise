@echo off

cd %~dp0

cd ../..
echo -------------Message.jar---------------------------
cd ./Message
call ant -buildfile ./build_no_api.xml default
if not exist ../jar/Message.jar (
	msg compile error
)

echo -------------CorgiServerCore.jar---------------------------
cd ..
cd ./CorgiServerCore
call ant -buildfile ./build_no_api.xml default
if not exist ../jar/CorgiServerCore.jar (
	msg compile error
)

echo -------------ManageServer.jar---------------------------
cd ..
cd ./ManageServer
call ant -buildfile ./build_no_api.xml default
if not exist ../jar/ManageServer.jar (
	msg compile error
)

echo -------------InterfaceServer.jar---------------------------
cd ..
cd ./InterfaceServer
call ant -buildfile ./build_no_api.xml default
if not exist ../jar/InterfaceServer.jar (
	msg compile error
)

echo -------------AuthServer.jar---------------------------
cd ..
cd ./AuthServer
call ant -buildfile ./build_no_api.xml default
if not exist ../jar/AuthServer.jar (
	msg compile error
)

echo -------------GameServer.jar---------------------------
cd ..
cd ./GameServer
call ant -buildfile ./build_no_api.xml default
if not exist ../jar/GameServer.jar (
	msg compile error
)

echo -------------ClubServer.jar---------------------------
cd ..
cd ./ClubServer
call ant -buildfile ./build_no_api.xml default
if not exist ../jar/ClubServer.jar (
	msg compile error
)

echo -------------BattleServer.jar---------------------------
cd ..
cd ./BattleServer
call ant -buildfile ./build_no_api.xml default
if not exist ../jar/BattleServer.jar (
	msg compile error
	pause
	goto end
)

cd ..\..
echo -----Success !!!-----------
