@rem This file is called to compile a single .c file into a Windows executable.
@rem This batch receives the name of the input file WITHOUT .c EXTENSION as first and unique parameter.

@rem Please change the rest of the file, accordingly to your C compiler.
@rem The below example is using MS VS 2017 and assumes it is running in Developer Command Prompt.
	
cl %1.c /Fo: %1.obj /Fe: %1.exe
@if exist %1.obj del %1.obj
