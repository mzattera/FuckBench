@rem This file is called to compile a single .c file into a Windows executable.
@rem This batch receives the name of the input file as first and unique parameter.

@rem Please change the rest of the file, accordingly to your C compiler.
@rem The below example is using MS VS 2017 and assumes it is running in Developer Command Prompt.
	
rem cl %1 /Fo: %1.obj /Fe: %1.exe
rem if exist %1.obj del %1.obj
