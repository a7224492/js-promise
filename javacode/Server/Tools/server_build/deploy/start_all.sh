#!/bin/bash

cd ./manager/bin
python ManagerServer.py start

cd ../../auth/bin
python AuthServer.py start

cd ../../battle/bin
python BattleServer.py start

cd ../../club/bin
python ClubServer.py start

cd ../../game/bin
python GameServer.py start

cd ../../replay/bin
python ReplayServer.py start

cd ../../interface/bin
python InterfaceServer.py start