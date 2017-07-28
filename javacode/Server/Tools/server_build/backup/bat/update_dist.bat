::�����淨������jar��·��
:set_battle_jar_path
	set battle_jar_path=%native_server_path%\jar

::����淨������jar��
:clear_battle_jar
	del /s/q %battle_jar_path%\*.*

::���淨������jar��
:build_battle_jar
	call one_click_release_all

::�����з�������jar�����𵽴��Ŀ¼
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

::����������⵽���Ŀ¼
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