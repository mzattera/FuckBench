@echo off

rem This re-builds library used for the cc65 compiler
rem If the build is successful 6502bf.lib is created, otherwise any existing version is removed.

rem Configuration
set "FB_LIB=%~dp0%"
if not "%FB_LIB:~-1%"=="\" set "FB_LIB=%FB_LIB%\"
set "FB_HOME=%FB_LIB%.."
call "%FB_HOME%\bin\FB_config.bat"
@echo off

cd %FB_LIB%
if exist *.o del /F /Q *.o
if exist *.lib del /F /Q *.lib

copy /Y "%FB_CC65%\lib\none.lib" 6502bf.lib
copy /Y "%FB_CC65%\asminc\zeropage.inc" .

for /r %%i in (.\*.s) do (
	echo %%i
	if exist 6502bf.lib (
		"%FB_CC65%\bin\ca65" "%%i"
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
