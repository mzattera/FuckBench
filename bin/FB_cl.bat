@echo off

rem This compiles %1.c into %1.exe

rem In the process it creates:
rem     %1_c.bf with BrainFuck code.
rem     %1_c.c with C version of BrainFuck code.

rem Configuration
set "FB_BIN=%~dp0%"
if not "%FB_BIN:~-1%"=="\" set "FB_BIN=%FB_BIN%\"
call "%FB_BIN%FB_config.bat"

if exist %1.out del /F /Q %1.out
if exist %1_c.bf del /F /Q %1_c.bf
if exist %1_c.c del /F /Q %1_c.c
if exist %1_c.exe del /F /Q %1_c.exe
if exist %1.exe del /F /Q %1.exe

rem compiles %1.c into 6502 code
echo on
"%FB_CC65%\bin\cl65" --cpu 6502 -Oi -t none -C "%FB_CLIB%\6502bf.cfg" -o %1.out %1.c "%FB_CLIB%\6502bf.lib"

@if exist %1.out (
	rem merges 6502bf.bf and %1.out into %1_c.bf
	echo.
	java -jar "%FB_BIN%Linker.jar" %1.out %1_c.bf -i "%FB_HOME%\6502bf.bf"

	if errorlevel 0 (
		call "%FB_BIN%FB_bf.bat" %1_c
		if exist %1_c.exe move %1_c.exe %1.exe
		del /F /Q %1.out
	)	
)

