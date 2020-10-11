@echo off

rem This compiles any source file in test folder, runs it and then compares output with corresponding .ref file.
rem An error is issued if the output of the compiled code does not match content of .ref file.

rem Configuration
set "FB_TEST=%~dp0%"
if not "%FB_TEST:~-1%"=="\" set "FB_TEST=%FB_TEST%\"
set "FB_HOME=%FB_TEST%.."
call "%FB_HOME%\bin\FB_config.bat"

rem .bf files

cd "%FB_TEST%bf"
if exist *.exe del /F /Q *.exe
if exist *.out del /F /Q *.out

for /r %%i in (*.bf) do (
	echo %%i
	call "%FB_HOME%\bin\FB_bf.bat" "%%~ni" 
	if exist "%%~ni.exe" (
		"%%~ni.exe" > "%%~ni.out"
		fc "%%~ni.out" "%%~ni.ref" > nul
		if errorlevel 1 (
			goto failed_test
		)
	) else (
			goto failed_compile
	)
)

rem .fbf files

cd "%FB_TEST%fbf"
if exist *.exe del /F /Q *.exe
if exist *.out del /F /Q *.out

for /r %%i in (*.fbf) do (
	echo %%i
	call "%FB_HOME%\bin\FB_fbf.bat" "%%~ni"
	if exist "%%~ni.exe" (
		"%%~ni.exe" > "%%~ni.out"
		fc "%%~ni.out" "%%~ni.ref" > nul
		if errorlevel 1 (
			goto failed_test
		)
	) else (
			goto failed_compile
	)
)

:failed_test
	echo Test failed (output not matching corresponding .ref file).
	cd "%FB_TEST%"
	exit /B 1

:failed_compile
	echo File cannot be successfully compiled.
	cd "%FB_TEST%"
	exit /B 2

:success
	echo Test successful.
	cd "%FB_TEST%"
