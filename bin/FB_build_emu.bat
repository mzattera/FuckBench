@echo off

rem This compiles the FBF 6502 emulator (6502bf.fbf) into BF.

rem Configuration
set "FB_BIN=%~dp0%"
if not "%FB_BIN:~-1%"=="\" set "FB_BIN=%FB_BIN%\"
call "%FB_BIN%FB_config.bat"

if exist "%FB_HOME%\6502bf.bf" del /F /Q "%FB_HOME%\6502bf.bf"

"%FB_FBF%\LUA Binaries\LUA Binaries for Windows\lua5.1" "%FB_FBF%\FBF.lua" < "%FB_HOME%\6502bf.fbf" > "%FB_HOME%\6502bf.bf"

echo.
java -jar "%FB_BIN%CheckFile.jar" "%FB_HOME%\6502bf.bf"
if errorlevel 0 (
	echo Emulator built successfully.	
) else (
	if exist "%FB_HOME%\6502bf.bf" del /F /Q "%FB_HOME%\6502bf.bf"
	echo Build failed!
)