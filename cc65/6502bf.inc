;
; Macros for 6502bf BrainFuck emulator
;

; ---------------------------------------------------------------------------
; MACRO 
; These macro use invalid opcode $42 to trigger 6502bf emulator special behavior

.P02

; Prints a dump of 6502 registers
.macro EMU_DUMP   
	.byte $42, $00
.endmacro

; Quit emulator
.macro EMU_QUIT 
	.byte $42, $20
.endmacro

; Prints character which ASCII code is in the accumulator
.macro EMU_PUTC  
	.byte $42, $10
.endmacro

; Reads a character and stores its ASCII code in the accumulator 
; Z flag set if ASCII $00 is read, reset otherwise
.macro EMU_GETC  
	.byte $42, $11
.endmacro

; Prints given string
.macro EMU_PRINTS str   
	.byte $42, $12, str, $00
.endmacro

; Prints given string and goes new line
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
; The first byte following the SYSCALL opcode points to a zeropage location where a pointer to a memory buffer (LO byte, HI byte) is stored.
; The buffer contains a string.
; A & X must contain the number of characted in the buffer (LO & HI bytes respectively). THESE MUST NOT BE CHANGED.
; This prints buffer contents.
.macro EMU_CC65_WRITE ptr   
	.byte $42, $28, ptr
.endmacro

  
; 41 Reads a string as required by cc65 libraries.
; The first byte following the SYSCALL opcode points to a zeropage location where a pointer to a memory buffer (LO byte, HI byte) is stored.
; The buffer will store a string being read.
; A & X must contain the maximum number of characted in the buffer (LO & HI bytes respectively).
; This reads characters from input until desired count is reached or LF ($0A) is entered. Notice that $00 if found, will be replaced with $0A.
; Actual number of characters read is returned in A & X (LO & HI bytes respectively).
.macro EMU_CC65_READ ptr   
	.byte $42, $29, ptr
.endmacro


