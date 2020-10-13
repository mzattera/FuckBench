@echo off

rem This compiles %1.c into %1_c.exe

rem In the process it creates:
rem     %1_c.bf with BrainFuck code.
rem     %1_c.c with C version of BrainFuck code.

rem Configuration
set "FB_BIN=%~dp0%"
if not "%FB_BIN:~-1%"=="\" set "FB_BIN=%FB_BIN%\"
call "%FB_BIN%FB_config.bat"
set "FB_CLIB=%FB_BIN%..\cc65"

if exist %1.o del /F /Q %1.o
if exist %1.out del /F /Q %1.out
if exist %1_c.bf del /F /Q %1_c.bf
if exist %1_c.c del /F /Q %1_c.c
if exist %1_c.exe del /F /Q %1_c.exe

rem compiles %1.c into 6502 code
echo on

"%FB_CC65%\bin\cl65" -t none -C "%FB_CLIB%\6502bf.cfg" -o %1.out -Oir --cpu 6502 %2 %3 %4 %5 %7 %8 %9 %1.c "%FB_CLIB%\6502bf.lib"
@if exist %1.o del /F /Q %1.o

@if exist %1.out (
	rem merges 6502bf.bf and %1.out into %1_c.bf
	echo.
	java -jar "%FB_BIN%Linker.jar" %1.out %1_c.bf -i "%FB_HOME%\6502bf.bf"

	rem compiles %1_c.bf into C code %1_c.c
	if errorlevel 0 (
		call "%FB_BIN%FB_bf.bat" %1_c
	)
	
	del /F /Q %1.out
)
