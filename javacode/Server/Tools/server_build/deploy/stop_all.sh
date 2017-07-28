#!/bin/bash

cd ./manager/bin
python ManagerServer.py stop

cd ../../auth/bin
python AuthServer.py stop

cd ../../battle/bin
python BattleServer.py stop

cd ../../club/bin
python ClubServer.py stop

cd ../../game/bin
python GameServer.py stop

cd ../../replay/bin
python ReplayServer.py stop

cd ../../interface/bin
python InterfaceServer.py stop