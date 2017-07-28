@echo off

call :init %1 %2 %3
call :copy_prop
call :tar

pause
goto :eof

:init
	set area_name=%1
	set my_date=%2
	set commit_version=%3

	set packagePath=%~dp0
	set PATH=%PATH%;%packagePath%\..\software\7z;
	set biSource=%packagePath%\bi.properties
	set biTarget=%packagePath%\bitools\WEB-INF\classes\prop\bi.properties
	
	for /r %bi_path% %%a in (*.tar.gz) do ( del %%a )
	
	goto :eof
	
:copy_prop
    cd %packagePath%
    ren *.properties bi.properties
    copy %biSource% %biTarget%
	goto :eof
	
:tar
	cd %packagePath%
	set packageName=%area_name%_bitools_%my_date%_%commit_version%
	7z a %packageName%.tar bitools\WEB-INF\classes\prop\bi.properties
	7z a %packageName%.tar.gz %packageName%.tar
	del %packageName%.tar
    del %biTarget%
	goto :eof