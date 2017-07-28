::将配置文件部署到打包目录
:deploy_resource
::	copy %region_config_path%\manager.properties %deploy_path%\manager\resource
::	copy %region_config_path%\auth.properties %deploy_path%\auth\resource
::	copy %region_config_path%\auth.json %deploy_path%\auth\resource
::	copy %region_config_path%\game.properties %deploy_path%\game\resource
::	copy %region_config_path%\purchase.xml %deploy_path%\game\resource
::	copy %region_config_path%\rules.xml %deploy_path%\game\resource
::	copy %region_config_path%\server.json %deploy_path%\game\resource
::	copy %region_config_path%\wxpromoter.xml %deploy_path%\game\resource
::	copy %region_config_path%\securitygroups.xml %deploy_path%\game\resource
::	copy %region_config_path%\qqzeng-ip-utf8.dat %deploy_path%\game\resource
::	for /r %region_config_path% %%a in (*.sql) do ( copy "%%a" %deploy_path%\game\sql\ )
	
	 xcopy %region_config_path%\* %deploy_path%\ /s /h /d /y
