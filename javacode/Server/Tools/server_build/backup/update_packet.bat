@echo off

call %~dp0\bat\defines.bat

call %~dp0\bat\update_config.bat

call %~dp0\bat\update_dist.bat

::设置更新包名称
:name
	set package_name=%package_name%_dist

::将所有服务器jar包打入tar包
:dist
	cd %deploy_path%
	7z a %package_name%.tar auth\dist\ battle\dist\ club\dist\ game\dist\ interface\dist\ manager\dist\

::添加配置文件至tar包
:add_config
	set /p is_need_config="是否需要加配置文件(y/n:不区分大小写)"
	if "%is_need_config%" neq "y" if "%is_need_config%" neq "Y" goto :tar
	set /p add_config_file="请输入要添加的配置文件"
	set str="%add_config_file%"

	:add_config_loop
	for /f "delims=;, tokens=1,*" %%i in (%str%) do (
		echo %%i %%j
		set str="%%j"
		7z a %package_name%.tar %%i
		goto :add_config_loop
	)

::将tar包打为gz包
:tar
	7z a %package_name%.tar.gz %package_name%.tar
	del %package_name%.tar
	move %package_name%.tar.gz %packet_path%

pause