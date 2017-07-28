set key=kodgames

call :main %1 %2

:main
if %1==-add call :add %2
if %1==-show call :show %2
if %1==-gen ((call :gen %2)
	ren %2.jks %key%.jks
	del %2.jks)
exit /b %errorlevel%

:add
chcp 936
cls
call :gen %1
call :export %1 %1
call :import %1 %key%
del %1.jks
del %1.cer
exit /b %errorlevel%

:show
chcp 936
cls
keytool -list -v -keystore %1.jks -storepass kodstore
exit /b %errorlevel%

:gen
keytool -genkey -alias %1 -keystore %1.jks -storepass kodstore -keypass kodkey -keyalg RSA -dname "CN=%1, OU=kodunit, O=kodorg, L=kodlocal, ST=kodstate, C=kodcountry" -validity 3650
exit /b %errorlevel%

:export
keytool -export -alias %1 -keystore %1.jks -file %2.cer -storepass kodstore
exit /b %errorlevel%

:import
keytool -import -alias %1 -file %1.cer -keystore %2.jks -storepass kodstore -noprompt
exit /b %errorlevel%

