;
; Macros for 6502bf BrainFuck emulator
;

; ---------------------------------------------------------------------------
; MACRO 
; These macro use invalid opcode $42 to trigger 6502bf emulator special behavior

.P02

; 0 prints a dump of 6502 registers.
.macro EMU_DUMP   
	.byte $42, $00
.endmacro
  
; 32 quit emulator.
.macro EMU_QUIT 
	.byte $42, $20
.endmacro

; 16 prints the character coresponding to the ASCII value in the accumulator.
.macro EMU_PUTC  
	.byte $42, $10
.endmacro

; 17 reads a character into the accumulator and updates Z flag accordingly.
; Z flag set if ASCII $00 is read, reset otherwise.
.macro EMU_GETC  
	.byte $42, $11
.endmacro

; 18 prints string.
; The macro adds an additional $00 (NUL) char at the end of the string, so it must not be null terminated necessarily.
.macro EMU_PRINTS str   
	.byte $42, $12, str, $00
.endmacro

; 18 prints string and goes newline.
; The macro adds an additional $00 (NUL) char at the end of the string, so it must not be null terminated necessarily.
.macro EMU_PRINTLNS str   
	.byte $42, $12, str, $0a, $0d, $00
.endmacro

; 23 prints a string (absolute addressing) 
; The word following the SYSCALL opcode is used as a pointer to a 0-terminated string which will be will be printed.
.macro EMU_PRINTSAT ptr   
	.byte $42, $17
	.word ptr
.endmacro

; 19 prints hex byte value (absolute addressing) 
; The word following the SYSCALL opcode  used as a pointer to a memory cell whose content will be printed.
.macro EMU_PRINTBAT ptr   
	.byte $42, $13
	.word ptr
.endmacro

; 20 prints hex word value (absolute addressing) 
; The word following the SYSCALL opcode is used as a pointer to a word in memory (LO byte, HI byte) whose content will be printed.
.macro EMU_PRINTWAT ptr   
	.byte $42, $14
	.word ptr
.endmacro

; 21 prints hex byte value (zeropage addressing)
; The byte following the SYSCALL opcode is used as a pointer to a zeropage memory cell whose content will be printed.
.macro EMU_PRINTBZP ptr   
	.byte $42, $15, ptr
.endmacro

; 22 prints hex word value (zeropage addressing)
;The byte following the SYSCALL opcode is used as a pointer to a word in zeropage (LO byte, HI byte) whose content will be printed.
.macro EMU_PRINTWZP ptr   
	.byte $42, $16, ptr
.endmacro

; 40 Writes a string as required by cc65 libraries.
.macro EMU_CC65_WRITE
	.byte $42, $28
.endmacro

; 41 Reads a string as required by cc65 libraries.
.macro EMU_CC65_READ
	.byte $42, $29
.endmacro


