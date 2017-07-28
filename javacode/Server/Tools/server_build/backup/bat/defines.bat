::初始化打包环境
:init_packet_env
	set packet_path=%cd%
	del /q *.tar.gz
	
	set deploy_path=%packet_path%\deploy
	rd /s /q %deploy_path%
	
	for /f "delims==, tokens=1,2" %%i in (%packet_path%\info.txt) do (
		set %%i=%%j
		echo %%i=%%j
	)	

::设置工具路径
:set_tool_path
	set tool_path=%packet_path%\..
	set software_path=%tool_path%\software

	set svn_path=%software_path%\svn
	set compress_path=%software_path%\7z
	set ant_path=%software_path%\ant\bin
	set build_path=%tool_path%\one_click_release_all

	::把打包工具添加到path路径
	set PATH=%svn_path%;%compress_path%;%ant_path%;%build_path%;%PATH%

::设置本地服务器路径
:set_server_path
	set native_server_path=%packet_path%\..\..

:set_ccommon_path
	set native_common_path=%packet_path%\..\..\..\common
	
::更新服务器
:update_server
	svn update %native_server_path%
	svn update %native_common_path%
	
::获取svn信息，（server和common下的）
:get_svn_info_max
	call :get_svn_info %native_server_path% server_last_svn_info
	echo "use server svn version:"%server_last_svn_info%
	set svn_last_battle_change_vision=%server_last_svn_info%

::设置地区配置文件路径
:set_region_config_path
	set region_config_path=%native_server_path%\config\%region%

	if not exist %region_config_path% (
		echo "配置文件缺失："%region_config_path%
		pause
		exit
	)

::设置包名
:set_package_name
	set cur_date=%date%
	set cur_date=%cur_date:~0,10%
	set cur_date=%cur_date:/=%
	set package_name=%region%_server_%cur_date%_%svn_last_battle_change_vision%
	echo package_name=%package_name%
	
::获取svn信息
:get_svn_info
	cd %1
	echo %1
	svn info > f_svn_info
	
	if "%errorlevel%" equ "1" (
		echo "ERROR: svn版本过低，请使用1.8以上的版本"
		pause
		exit
	)

	findstr /n .* f_svn_info > f_svn_info_index
	del f_svn_info
	findstr /b "11" f_svn_info_index > f_svn_last_change_vision
	del f_svn_info_index
	for /f "delims=" %%i in (f_svn_last_change_vision) do ( set svn_last_change_vision=%%i )
	del f_svn_last_change_vision
	set svn_last_change_vision=%svn_last_change_vision:~-6%
	set "%2=%svn_last_change_vision: =%"
	echo %2=%svn_last_change_vision%