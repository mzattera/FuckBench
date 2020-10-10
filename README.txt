This folder is set to develop 6502 FuckBrainFuck emulator.

The folder CodeMerge is an Eclipse workbench with some Java utilities to automate dev pipeline.
These utilities should be exported as .jar files in the main folder.

The pipeline uses sevaral .bat files for automation; look into them for a detailed description of what they do.

The pipleine  assumes there is an installation of:

   a. FBF (FuckBrainFuck compiler)
   b. esotope-bfc BF to C compiler
   c. Visual Studio (including Developer Comand Prompt) to compile the C code
   d. cc65 6502 cross assmpler to compile 6502 programs
   e. Java 1.8 or higher to run the Java utilities

Above programs needs to be accessible from the .bat files.
   
   
1) Emulator file is 6502.fbf (this is used as default name in a couple places).
   It assumes 16 bit cells (emu requirement for simpler implementation) and wrapping (FBF requirment).
   

2) *** Use VisualStudio Developer Command Prompt *** before executing the below commands.
   A  link is provided in the main folder.


3) To compile FBF code file.fbf run:

     FBF file
   
   this will create a Window executable file.exe; in the process it also creates file.bf BrainFuck code and fbf.c.
   
     
3) To compile 6502 file.a into a Windows executable use:

     ASM file

   This will compile the 6502 code, "link" it with the emulator BrainFuck code (6502.bf) and create a Windows exceutable.
   Notice that 6502.bf with emulator code must already exist in order for the linking to succeed.
   Notice you can pass parameters to the CodeMerge call inisde the batch so that you can deside wher the 6502 is loaded in memory and the starting address for the program.


4) Calling 

     GO %1

   Will execute the above 2 steps in sequence; it will (re)comile the emulator, then compile 6502 assempby file, merge it with emulator and finally create a Windows executable %1.exe.

   
5) If desired, calling

     BF %1

   Will compule a BrainFuck fle into a Windows exceutable %1.exe (creating %1.c in the process).
   
6) Calling

	  REGRESSION
	
	will perform all regression tests in the ./test folder.
	See TestHarness.java for details.
	This batch file recompiles the emulator before running the tests;
	if you want to avoid that provide -x command line switch.

When changeing the emulator code, be mindful of where mem variable starts, this should always be the last variable/table and it contains 6502 memory.
You need to set CodeMerge.SKIP constant field to the starting address of the table, the rest of Java code will adjust accordingly.
This infomration is required to correctly populate 6502 code in the BrainFuck source.