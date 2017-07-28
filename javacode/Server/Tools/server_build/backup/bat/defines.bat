::��ʼ���������
:init_packet_env
	set packet_path=%cd%
	del /q *.tar.gz
	
	set deploy_path=%packet_path%\deploy
	rd /s /q %deploy_path%
	
	for /f "delims==, tokens=1,2" %%i in (%packet_path%\info.txt) do (
		set %%i=%%j
		echo %%i=%%j
	)	

::���ù���·��
:set_tool_path
	set tool_path=%packet_path%\..
	set software_path=%tool_path%\software

	set svn_path=%software_path%\svn
	set compress_path=%software_path%\7z
	set ant_path=%software_path%\ant\bin
	set build_path=%tool_path%\one_click_release_all

	::�Ѵ��������ӵ�path·��
	set PATH=%svn_path%;%compress_path%;%ant_path%;%build_path%;%PATH%

::���ñ��ط�����·��
:set_server_path
	set native_server_path=%packet_path%\..\..

:set_ccommon_path
	set native_common_path=%packet_path%\..\..\..\common
	
::���·�����
:update_server
	svn update %native_server_path%
	svn update %native_common_path%
	
::��ȡsvn��Ϣ����server��common�µģ�
:get_svn_info_max
	call :get_svn_info %native_server_path% server_last_svn_info
	echo "use server svn version:"%server_last_svn_info%
	set svn_last_battle_change_vision=%server_last_svn_info%

::���õ��������ļ�·��
:set_region_config_path
	set region_config_path=%native_server_path%\config\%region%

	if not exist %region_config_path% (
		echo "�����ļ�ȱʧ��"%region_config_path%
		pause
		exit
	)

::���ð���
:set_package_name
	set cur_date=%date%
	set cur_date=%cur_date:~0,10%
	set cur_date=%cur_date:/=%
	set package_name=%region%_server_%cur_date%_%svn_last_battle_change_vision%
	echo package_name=%package_name%
	
::��ȡsvn��Ϣ
:get_svn_info
	cd %1
	echo %1
	svn info > f_svn_info
	
	if "%errorlevel%" equ "1" (
		echo "ERROR: svn�汾���ͣ���ʹ��1.8���ϵİ汾"
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