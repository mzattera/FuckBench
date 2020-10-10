@echo off

rem Compiles 6502 assembler file %1.s then merges it with 6502bf emulator, finally the resulting code is compiled into an executable.

rem In the process it creates
rem     %1.bf with merged BrainFuck code (6502 emu + 6502 code to run).
rem     %1.c with merged BrainFuck code (6502 emu + 6502 code to run).

@rem Configuration
@set "FB_BIN=%~dp0%"
@if not "%FB_BIN:~-1%"=="\" set "FB_BIN=%FB_BIN%\"
@call "%FB_BIN%FB_config.bat"
@set "FB_HOME=%FB_BIN%.."

@if exist %1.bf del /F /Q %1.bf
@if exist %1.c del /F /Q %1.c
@if exist %1.exe del /F /Q %1.exe

@rem compiles %1.ca65 into %1.out

"%FB_CC65%\bin\ca65" -o %1.o %1.s
@if exist %1.o (
	"%FB_CC65%\bin\ld65" -C "%FB_HOME%\cc65\6502bf.cfg" -o %1.out %1.o
	del /F /Q %1.o
)

@rem merges 6502bf.bf and %1.out into %1.bf

@if exist %1.out (
	echo.
	echo Invoking Java linker	
	java -jar "%FB_BIN%Linker.jar" %1.out %1.bf -i "%FB_HOME%\6502bf.bf" %2 %3 %4 %5 %6 %7 %8 %9

	rem compiles %1.bf into C code %1.c
	if errorlevel 0 (
		echo.
		call "%FB_BIN%FB_bf.bat" %1
		del /F /Q %1.out
	)
)

