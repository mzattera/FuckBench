@rem This compiles %1.fbf into %1.exe

@rem In the process it creates:
@rem     %1.bf with BrainFuck code.
@rem     %1.c with C code.

@rem Configuration
@set "FB_BIN=%~dp0%"
@if not "%FB_BIN:~-1%"=="\" set "FB_BIN=%FB_BIN%\"
@call "%FB_BIN%FB_config.bat"

@if exist %1.bf del /F /Q %1.bf
@if exist %1.c del /F /Q %1.c

@rem compiles FBF %1.fbf into BF %1.bf

"%FB_FBF%\LUA Binaries\LUA Binaries for Windows\lua5.1" "%FB_FBF%\FBF.lua" < %1.fbf > %1.bf

@rem compiles %1.bf into C code %1.exe

@call "%FB_BIN%FB_bf.bat" %1