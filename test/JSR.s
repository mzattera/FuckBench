; Pseudo-mnemonic for illegal instruction
; 6502bf will read next byte and perform a "SYStem CALL" accordingly
SYSCALL 	= $42	
DUMP 		= $00
PRINT 		= $10
READ 		= $11
PRINTSTR	= $12
EXIT		= $20
		
		JSR sub1
		.byte SYSCALL, PRINTSTR, "Returned from sub.", 13, 10, 0

; Check infinite loop detection		
here:	JSR here

		.byte SYSCALL, PRINTSTR, "You should not be here.", 13, 10, 0
		.byte SYSCALL, EXIT	
		
sub1:	.byte SYSCALL, PRINTSTR, "Entered sub.", 13, 10, 0
		RTS
		
		.byte SYSCALL, PRINTSTR, "You should not be here.", 13, 10, 0
		.byte SYSCALL, EXIT	