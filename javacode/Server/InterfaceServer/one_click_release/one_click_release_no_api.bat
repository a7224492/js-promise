cd ..

call ant -buildfile ./build_no_api.xml default

if not exist ../jar/InterfaceServer.jar (
	msg compile error
	goto end
)

echo -----Success !!!-----------
:end
echo press any key to continue
pause
