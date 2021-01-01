@echo off
rem Configures FuckBench



rem === Below section sets 3rd party tools  ===========================================
rem *** YOu MUST change this section based on your machine setup ***

rem cc65 home. NOTICE IT MUST *NOT* CONTAIN SPACES and must NOT have a trailing '\'
set "FB_CC65=D:\Programs\cc65"


rem Python Executable.
rem This is optional and only needed if you want to generate C out of BF code.
rem You can leave it unset but compilation will stop after generationg BrainFuck code.

rem set FB_PYTHON=
rem set "FB_PYTHON=D:\Programs\WPy64-2.7.13.1Zero\python-2.7.13.amd64\python.exe"



rem === Below section sets platform folders ============================================
rem *** Do not change this section *** unless you know what you are doing.

rem bin and <root> folders
set "FB_BIN=%~dp0%"
if not "%FB_BIN:~-1%"=="\" set "FB_BIN=%FB_BIN%\"
set "FB_HOME=%FB_BIN%.."

rem 6502 functional test folders
set "FB_FT=%FB_HOME%\test\ca65"

rem cc65 libraries for 6502bf folder
set "FB_CLIB=%FB_HOME%\cc65"

rem b2fJ redistibution folder
set "FB_B2FJ=%FB_HOME%\redistr\b2fJ"

rem FuckBrainFuck redistribution folder
set "FB_FBF=%FB_HOME%\redistr\FBF 1.7.1"

rem Esotope redistribution folder
set "FB_ESOTOPE=%FB_HOME%\redistr\esotope-bfc-master 2009.12.27"
