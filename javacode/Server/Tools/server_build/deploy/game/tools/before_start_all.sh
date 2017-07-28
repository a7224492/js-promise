#!/bin/sh
rm ../../[bim]*/bin/zdb/* -rf
rm ../../[abgim]*/bin/*.pid -f

ps aux | grep AuthServer | awk '{print $2}' > tmp
#!/bin/bash
while read line
do
kill -9 $line
done < tmp

ps aux | grep BattleServer | awk '{print $2}' > tmp
#!/bin/bash
while read line
do
kill -9 $line
done < tmp

ps aux | grep GameServer | awk '{print $2}' > tmp
#!/bin/bash
while read line
do
kill -9 $line
done < tmp

ps aux | grep InterfaceServer | awk '{print $2}' > tmp
#!/bin/bash
while read line
do
kill -9 $line
done < tmp

ps aux | grep ManageServer| awk '{print $2}' > tmp
#!/bin/bash
while read line
do
kill -9 $line
done < tmp

rm -f tmp
