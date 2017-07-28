::设置玩法服务器jar包路径
:set_battle_jar_path
	set battle_jar_path=%native_server_path%\jar

::清除玩法服务器jar包
:clear_battle_jar
	del /s/q %battle_jar_path%\*.*

::打玩法服务器jar包
:build_battle_jar
	call one_click_release_all

::将所有服务器的jar包部署到打包目录
:deploy_dist
	mkdir %deploy_path%\auth\dist
	mkdir %deploy_path%\game\dist
	mkdir %deploy_path%\club\dist
	mkdir %deploy_path%\battle\dist
	mkdir %deploy_path%\interface\dist
	mkdir %deploy_path%\manager\dist

	copy %battle_jar_path%\BattleServer.jar %deploy_path%\battle\dist
	copy %battle_jar_path%\Message.jar %deploy_path%\battle\dist

	copy %battle_jar_path%\AuthServer.jar %deploy_path%\auth\dist
	copy %battle_jar_path%\InterfaceServer.jar %deploy_path%\interface\dist
	copy %battle_jar_path%\ManageServer.jar %deploy_path%\manager\dist
	copy %battle_jar_path%\GameServer.jar %deploy_path%\game\dist
	copy %battle_jar_path%\ClubServer.jar %deploy_path%\club\dist

	copy %battle_jar_path%\CorgiServerCore.jar %deploy_path%\auth\dist
	copy %battle_jar_path%\CorgiServerCore.jar %deploy_path%\game\dist
	copy %battle_jar_path%\CorgiServerCore.jar %deploy_path%\club\dist
	copy %battle_jar_path%\CorgiServerCore.jar %deploy_path%\battle\dist
	copy %battle_jar_path%\CorgiServerCore.jar %deploy_path%\interface\dist
	copy %battle_jar_path%\CorgiServerCore.jar %deploy_path%\manager\dist

	copy %battle_jar_path%\PlatformMessage.jar %deploy_path%\auth\dist
	copy %battle_jar_path%\PlatformMessage.jar %deploy_path%\game\dist
	copy %battle_jar_path%\PlatformMessage.jar %deploy_path%\club\dist
	copy %battle_jar_path%\PlatformMessage.jar %deploy_path%\battle\dist
	copy %battle_jar_path%\PlatformMessage.jar %deploy_path%\interface\dist
	copy %battle_jar_path%\PlatformMessage.jar %deploy_path%\manager\dist

::部署第三方库到打包目录
:lib
	mkdir %deploy_path%\auth\lib
	mkdir %deploy_path%\battle\lib
	mkdir %deploy_path%\game\lib
	mkdir %deploy_path%\club\lib
	mkdir %deploy_path%\interface\lib
	mkdir %deploy_path%\manager\lib

	set third_lib_path=%native_server_path%\third_lib\jar
	xcopy /s /h /d /c /y %third_lib_path%\* %deploy_path%\auth\lib
	xcopy /s /h /d /c /y %third_lib_path%\* %deploy_path%\battle\lib
	xcopy /s /h /d /c /y %third_lib_path%\* %deploy_path%\game\lib
	xcopy /s /h /d /c /y %third_lib_path%\* %deploy_path%\club\lib
	xcopy /s /h /d /c /y %third_lib_path%\* %deploy_path%\interface\lib
	xcopy /s /h /d /c /y %third_lib_path%\* %deploy_path%\manager\lib