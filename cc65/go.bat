copy /Y none.lib 6502bf.lib

D:\Programs\cc65\bin\ca65 crt0.s
D:\Programs\cc65\bin\ar65 a 6502bf.lib crt0.o
D:\Programs\cc65\bin\ca65 interrupt.s
D:\Programs\cc65\bin\ar65 a 6502bf.lib interrupt.o
D:\Programs\cc65\bin\ca65 read.s
D:\Programs\cc65\bin\ar65 a 6502bf.lib read.o
D:\Programs\cc65\bin\ca65 write.s
D:\Programs\cc65\bin\ar65 a 6502bf.lib write.o
D:\Programs\cc65\bin\cc65 -t none -O %1.c
D:\Programs\cc65\bin\ca65 %1.s
D:\Programs\cc65\bin\ld65 -C 6502bf.cfg -m %1.map %1.o 6502bf.lib
del /F /Q *.o
java -jar ..\CodeMerge.jar a.out %1_bf.bf -i ..\6502bf.bf


del /F /Q %1_bf.c
"D:\Programs\WPy64-2.7.13.1Zero\python-2.7.13.amd64\python.exe" "D:\Programs\esotope-bfc\esotope-bfc" %1_bf.bf > %1_bf.c
java -jar ..\TweakEmu.jar -i %1_bf.c -o %1_bf.c
cl %1_bf.c /Fo: %1_bf.obj /Fe: %1_bf.exe
del %1_bf.obj