@rem This builds 6502 functional tests contained in this folder.
@rem You *MUST* pass it the name of the .ca65 file to build (without .ca65 extension).

@rem Configuration
@set "FB_FT=%~dp0%"
@if not "%FB_FT:~-1%"=="\" set "FB_FT=%FB_FT%\"
@set "FB_BIN=%FB_FT%..\..\bin\"
@call "%FB_BIN%FB_config.bat"
@set "FB_HOME=%FB_BIN%.."

@if exist %1.bin del /F /Q %1.bin

"%FB_CC65%\bin\ca65" -o %1.o %1.ca65
"%FB_CC65%\bin\ld65" -C "%FB_FT%example.cfg" -o %1.bin %1.o

@if exist %1.o del /F /Q %1.o

@rem merges 6502bf.bf and %1.out into %1.bf

@if exist %1.bin (
	echo.
	echo Invoking Java linker	
	java -jar "%FB_BIN%Linker.jar" %1.bin %1.bf -i "%FB_HOME%\6502bf.bf" -a 1024 -l 0

	rem compiles %1.bf into C code %1.c
	if errorlevel 0 (
		echo.
		call "%FB_BIN%FB_bf.bat" %1
	)

	del /F /Q %1.bin
)
