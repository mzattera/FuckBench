@echo off

rem this compiles %1.bf into %1.exe

rem In the process it creates:
rem     %1.c with C code.

del /F /Q %1.c

rem compiles %1.bf into C code %1.c

"D:\Programs\WPy64-2.7.13.1Zero\python-2.7.13.amd64\python.exe" "D:\Programs\esotope-bfc\esotope-bfc" %1.bf > %1.c
java -jar TweakEmu.jar -i %1.c -o %1.c

rem compiles %1.c into %1.exe

cl %1.c /Fo: %1.obj /Fe: %1.exe
del %1.obj