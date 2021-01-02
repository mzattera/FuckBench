@echo off

rem This compiles %1.java into %1.exe

rem In the process it creates:
rem     %1.bf with BrainFuck code.
rem     %1.c with C code.

rem Configuration
set "FB_BIN=%~dp0%"
if not "%FB_BIN:~-1%"=="\" set "FB_BIN=%FB_BIN%\"
call "%FB_BIN%FB_config.bat"


echo on

@rem Compile .java to .class
javac -source 1.8 -target 1.8 -bootclasspath "%FB_B2FJ%\lib\classes.jar" -classpath ".;%CLASSPATH%" %1.java %2 %3 %4 %5 %6 %7 %8 %9
@if ERRORLEVEL 1 goto end

@rem "Link" .class to java_code.h
@set "FB_LINK_CLASSPATH=.;%CLASSPATH%;%FB_B2FJ%\redistr\lib\bcel-5.1.jar;%FB_B2FJ%\redistr\lib\commons-cli-1.0.jar;%FB_B2FJ%\lib\jtools.jar;%FB_B2FJ%\lib\classes.jar"

java -classpath "%FB_LINK_CLASSPATH%" js.tinyvm.TinyVM --writeorder LE --classpath "%FB_LINK_CLASSPATH%" -o java_code.h "%~n1"


@echo off
echo.
echo === Compiling b2fJ JVM...
echo.

del /F /Q %1.class

if ERRORLEVEL 1 goto end
MOVE /Y java_code.h "%FB_B2FJ%"\src\platform\fb
if ERRORLEVEL 1 goto end

rem moves into C source folder, this saves us from issues with spaces in folder names and cc65
set "FB_CD=%CD%"
if exist %1.out del /F /Q %1.out
cd "%FB_B2FJ%\src"
set "FB_BUILD=_tmp_build"

rem Clears temp folder where we will build the JVM
if exist %FB_BUILD% rmdir /S /Q %FB_BUILD%
mkdir %FB_BUILD%

rem Remove old .o files
if exist javavm\*.o del /S /Q /F javavm\*.o
if exist platform\fb\*.o del /S /Q /F platform\fb\*.o

set "CC=%FB_CC65%%\bin\cl65"
set "CC_PARAMS=-c --cpu 6502 -Oi -t none -I .\javavm -I .\platform\fb -W -unused-param,-unused-var"
set "CC_CLI=%CC% %CC_PARAMS%"

%CC_CLI% javavm\exceptions.c
%CC_CLI% javavm\interpreter.c
%CC_CLI% javavm\language.c
%CC_CLI% javavm\threads.c
%CC_CLI% javavm\memory.c
%CC_CLI% javavm\nativeemul.c
%CC_CLI% javavm\tvmemul.c
%CC_CLI% javavm\trace.c

%CC_CLI% platform\fb\platform_native.c
%CC_CLI% platform\fb\main.c

move /Y javavm\*.o %FB_BUILD%
move /Y platform\fb\*.o %FB_BUILD%

cd "%FB_BUILD%"

set "CL=%FB_CC65%%\bin\ld65"
set "CL_PARAMS=-L "%FB_CLIB%" -C "%FB_CLIB%\6502bf.cfg" -o %1.out"
set "CL_CLI=%CL% %CL_PARAMS%"

%CL_CLI% exceptions.o interpreter.o language.o main.o memory.o nativeemul.o platform_native.o threads.o tvmemul.o trace.o 6502bf.lib

java -jar "%FB_BIN%CheckFile.jar" %1.out
if not errorlevel 0 goto end

rem moves back all prg files into original folder and goes back there
move /Y %1.out "%FB_CD%"
cd ..
rmdir /S /Q %FB_BUILD%
cd "%FB_CD%""

echo.
echo =========================
echo.

rem links program into .bf and compiles it
if exist %1.exe del /F /Q %1.exe

java -jar "%FB_BIN%Linker.jar" %1.out %1.bf -i "%FB_HOME%\6502bf.bf" %2 %3 %4 %5 %6 %7 %8 %9
del /F /Q %1.out

rem compiles %1.bf into C code %1.c
if errorlevel 0 (
	call "%FB_BIN%FB_bf.bat" %1
)

:end
	@exit /B
