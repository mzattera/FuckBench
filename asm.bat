@echo off

rem Compiles 6502 assembler file %1.s then merges it with 6502bf emulator, finally the resulting code is compiled into an executable.

rem In the process it creates
rem     %1.txt file with the listing from the assembler
rem     %1.bf with merged BrainFuck code (6502 emu + 6502 code to run).
rem     %1.c with merged BrainFuck code (6502 emu + 6502 code to run).

del /F /Q %1.txt
del /F /Q %1.bf
del /F /Q %1.c
del /F /Q %1.exe

rem compiles %1.s into %1.out

D:\Programs\cc65\bin\ca65 -l %1.txt -o %1.o %1.s
D:\Programs\cc65\bin\ld65 -C .\cc65\6502bf.cfg -o %1.out %1.o
del /F /Q %1.o

rem merges 6502bf.bf and %1.out into %1.bf

java -jar CodeMerge.jar %1.out %1.bf %2 %3 %4 %5 %6 %7 %8 %9
del /F /Q %1.out

rem compiles %1.bf into C code %1.c

call bf.bat %1
