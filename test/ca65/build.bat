REM builds the test. Pass it the name of the .ca65 file to build.

D:\Programs\cc65\bin\ca65 -l %1.txt -o %1.o %1.ca65
D:\Programs\cc65\bin\ld65  -C example.cfg -o %1.bin %1.o

del /F /Q %1.o

java -jar ..\..\CodeMerge.jar %1.bin %1.bf -i ..\..\6502bf.bf -a 1024 -l 0

del /F /Q %1.bin

"D:\Programs\WPy64-2.7.13.1Zero\python-2.7.13.amd64\python.exe" "D:\Programs\esotope-bfc\esotope-bfc" %1.bf > %1.c
java -jar ..\..\TweakEmu.jar -i %1.c -o %1.c

cl %1.c /Fo: %1.obj /Fe: %1.exe
del /F /Q %1.c
del %1.obj
