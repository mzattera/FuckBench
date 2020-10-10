@echo off

rem this compiles %1.fbf into %1.exe

rem In the process it creates:
rem     %1.bf with BrainFuck code.
rem     %1.c with C code.

del /F /Q %1.bf
del /F /Q %1.c

rem compiles FBF %1.fbf into BrainFuck %1.bf

"D:\Programs\FBF\LUA Binaries\LUA Binaries for Windows\lua5.1" "D:\Programs\FBF\FBF.lua" < %1.fbf > %1.bf

rem compiles %1.bf into C code %1.exe

call bf.bat %1