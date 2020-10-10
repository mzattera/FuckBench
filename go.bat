@echo off

rem Compiles 6502 assembly %1.a into %1.exe after linking it with 6502bf FBF emulator.

rem Compiles the emulator

call fbf.bat 6502bf

rem Compiles %1.a %1.exe merging with emulator code

call asm.bat %1
