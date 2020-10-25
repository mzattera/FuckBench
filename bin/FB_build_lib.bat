@echo off

rem This re-builds library used for the cc65 compiler
rem If the build is successful 6502bf.lib is created, otherwise any existing version is removed.

rem Configuration
set "FB_BIN=%~dp0%"
if not "%FB_BIN:~-1%"=="\" set "FB_BIN=%FB_BIN%\"
call "%FB_BIN%FB_config.bat"

cd "%FB_CLIB%"
if exist *.o del /F /Q *.o
if exist 6502bf.lib del /F /Q 6502bf.lib

copy /Y "%FB_CC65%\lib\none.lib" 6502bf.lib

for %%i in (*.s) do (
	echo %%i
	if exist 6502bf.lib (
		"%FB_CC65%\bin\ca65" %FB_CA65_PARAMS% "%%i"
		if exist "%%~ni.o" (
			"%FB_CC65%\bin\ar65" a 6502bf.lib "%%~ni.o"
		) else (
			del /F /Q *.lib
		)
	)
)

for %%i in (*.c) do (
	echo %%i
	if exist 6502bf.lib (
		call "%FB_BIN%FB_cc.bat" "%%~ni"
		if exist "%%~ni.o" (
			"%FB_CC65%\bin\ar65" a 6502bf.lib "%%~ni.o"
		) else (
			del /F /Q *.lib
		)
	)
)

if exist *.o del /F /Q *.o

echo.
if exist 6502bf.lib (
	echo Build succeeded.
) else (
	echo Build terminated with errors.
)
