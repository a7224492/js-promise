@echo off

call %~dp0\bat\defines.bat

call %~dp0\bat\update_config.bat

call %~dp0\bat\update_dist.bat

::������������
:name
	set package_name=%package_name%_all

::��������������Դ��Ϊtar��
:tar
	cd %deploy_path%
	7z a %package_name%.tar auth\ battle\ game\ club\ interface\ manager\ start_all.sh stop_all.sh
	7z a %package_name%.tar.gz %package_name%.tar
	del %package_name%.tar
	move %package_name%.tar.gz %packet_path%

pause