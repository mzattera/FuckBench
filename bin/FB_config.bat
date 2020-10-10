@echo off

rem Configures FuckBench

rem Python Executable.
rem This is optional and only needed if you want to generate C out of BF code.
rem You can leave it unset but compilation will stop after generationg BrainFuck code.

rem set FB_PYTHON=
set "FB_PYTHON=D:\Programs\WPy64-2.7.13.1Zero\python-2.7.13.amd64\python.exe"



rem === NO CHANGES NEEDED BELOW ============================================================
rem ...unless you really know what you are doing

rem bin and <root> folders
set "FB_BIN=%~dp0%"
if not "%FB_BIN:~-1%"=="\" set "FB_BIN=%FB_BIN%\"
set "FB_HOME=%FB_BIN%.."

rem Folder where FuckBrainFuck is installed.
set "FB_FBF=%FB_HOME%\redistr\FBF 1.7.1"

rem Esotope BF 2 C compiler.
set "FB_ESOTOPE=%FB_HOME%\redistr\esotope-bfc-master 2009.12.27"

echo on