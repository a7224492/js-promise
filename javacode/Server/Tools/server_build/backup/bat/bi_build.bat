::ѡ���Ƿ��BI��
:check_input
	set /p is_need_bi="�Ƿ������淨����Ҫ��BI������(y/n:�����ִ�Сд)"
	if "%is_need_bi%" neq "y" if "%is_need_bi%" neq "Y" goto :eof

::��BI��
:build_bi
	set bi_path=%tool_path%\bi_build
	copy %region_config_path%\bi.properties %bi_path%
	echo DEBUG: svn_last_change_vision=%svn_last_change_vision%
	call %bi_path%\build.bat %region% %cur_date% %svn_last_change_vision%
	for /r %bi_path% %%a in (*.tar.gz) do ( move "%%a" %~dp0\.. )