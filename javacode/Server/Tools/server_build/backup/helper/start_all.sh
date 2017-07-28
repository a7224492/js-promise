#!/bin/sh

#各个服务器调试模式的默认端口号
#auth default port: 11000
#battle default port: 11001
#game default port: 11002
#interface default port: 11003
#manager default port: 11004

param=(
$1
$2
$3
$4
$5
)

for i in 0 1 2 3 4
do
	if [ ! -n "${param[$i]}" ] || ! [ "${param[$i]}" -gt 0 ] 2>/dev/null ;then 
		param[$i]=`expr 11000 + $i`;
	fi 
done

auth_port=${param[0]}
battle_port=${param[1]}
game_port=${param[2]}
interface_port=${param[3]}
manager_port=${param[4]}

echo $auth_port $battle_port $game_port $interface_port $manager_port

cd ./manager/bin
python ManagerServer.py debug $manager_port

cd ../../auth/bin
python AuthServer.py debug $auth_port

cd ../../battle/bin
python BattleServer.py debug $battle_port

cd ../../game/bin
python GameServer.py debug $game_port

cd ../../interface/bin
python InterfaceServer.py debug $interface_port
