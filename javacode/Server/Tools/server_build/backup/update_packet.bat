@echo off

call %~dp0\bat\defines.bat

call %~dp0\bat\update_config.bat

call %~dp0\bat\update_dist.bat

::���ø��°�����
:name
	set package_name=%package_name%_dist

::�����з�����jar������tar��
:dist
	cd %deploy_path%
	7z a %package_name%.tar auth\dist\ battle\dist\ club\dist\ game\dist\ interface\dist\ manager\dist\

::��������ļ���tar��
:add_config
	set /p is_need_config="�Ƿ���Ҫ�������ļ�(y/n:�����ִ�Сд)"
	if "%is_need_config%" neq "y" if "%is_need_config%" neq "Y" goto :tar
	set /p add_config_file="������Ҫ��ӵ������ļ�"
	set str="%add_config_file%"

	:add_config_loop
	for /f "delims=;, tokens=1,*" %%i in (%str%) do (
		echo %%i %%j
		set str="%%j"
		7z a %package_name%.tar %%i
		goto :add_config_loop
	)

::��tar����Ϊgz��
:tar
	7z a %package_name%.tar.gz %package_name%.tar
	del %package_name%.tar
	move %package_name%.tar.gz %packet_path%

pause