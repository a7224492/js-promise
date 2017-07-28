cd ..
call .\bat\defines.bat

java -cp %my_jar_path% com.kodgames.main.StartAllOnLinux %server_ip% %server_user% %server_pwd% %area_name%

pause