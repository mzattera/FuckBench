@echo off

rem Compiles 6502 assembler file %1.s then merges it with 6502bf emulator, finally the resulting code is compiled into an executable.

rem In the process it creates
rem     %1.bf with merged BrainFuck code (6502 emu + 6502 code to run).
rem     %1.c with merged BrainFuck code (6502 emu + 6502 code to run).

rem Configuration
set "FB_BIN=%~dp0%"
if not "%FB_BIN:~-1%"=="\" set "FB_BIN=%FB_BIN%\"
call "%FB_BIN%FB_config.bat"

if exist %1.o del /F /Q %1.o
if exist %1.out del /F /Q %1.out
if exist %1.bf del /F /Q %1.bf
if exist %1.c del /F /Q %1.c
if exist %1.exe del /F /Q %1.exe

rem compiles %1.ca65 into %1.out
echo on

"%FB_CC65%\bin\ca65" %FB_CA65_PARAMS% -o %1.o %1.s
@java -jar "%FB_BIN%CheckFile.jar" %1.o
if errorlevel 0 (
	"%FB_CC65%\bin\ld65" %FB_LA65_PARAMS% -o %1.out %1.o 6502bf.lib

	@del /F /Q %1.o
)

@echo off
rem merges 6502bf.bf and %1.out into %1.bf

java -jar "%FB_BIN%CheckFile.jar" %1.out
if errorlevel 0 (
	echo.
	java -jar "%FB_BIN%Linker.jar" %1.out %1.bf -i "%FB_HOME%\6502bf.bf" %2 %3 %4 %5 %6 %7 %8 %9

	rem compiles %1.bf into C code %1.c
	if errorlevel 0 (
		call "%FB_BIN%FB_bf.bat" %1
	)

	del /F /Q %1.out
)

