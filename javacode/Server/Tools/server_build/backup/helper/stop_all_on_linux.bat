cd ..
call .\bat\defines.bat

java -cp %my_jar_path% com.kodgames.main.StopAllOnLinux %server_ip% %server_user% %server_pwd% %area_name%

pause