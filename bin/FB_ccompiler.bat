@rem This file is called to compile a single .c file into a Windows executable.
@rem This batch receives the name of the input file WITHOUT .c EXTENSION as first and unique parameter.
@rem It shoudl output an .exe file with same name than the input file.

@rem Please change the rest of the file, accordingly to your C compiler.



@rem The below example is using GCC.

gcc -O3 -o %1.exe %1.c



@rem The below example is using MS VS 2017 and assumes it is running in Developer Command Prompt.
	
@rem cl %1.c /Fo: %1.obj /Fe: %1.exe
@rem @if exist %1.obj del %1.obj


