@echo off

rem This runs a set of tests; in the process it rebuilds some assets too.
rem See documentation about how to create a new release to see which parameters are available.

rem Configuration
set "FB_BIN=%~dp0%"
if not "%FB_BIN:~-1%"=="\" set "FB_BIN=%FB_BIN%\"
call "%FB_BIN%FB_config.bat"

java -jar "%FB_BIN%TestRunner.jar" "%FB_HOME%" %1 %2 %3 %4 %5 %6 %7 %8 %9