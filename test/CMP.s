; Pseudo-mnemonic for illegal instruction
; 6502bf will read next byte and perform a "SYStem CALL" accordingly
SYSCALL 	= $42	
DUMP 		= $00
PRINT 		= $10
READ 		= $11
PRINTSTR	= $12
EXIT		= $20

	LDA #0
	CMP #100
	.byte SYSCALL, DUMP
	
	LDX #100
	CPX #100
	.byte SYSCALL, DUMP
	
	LDY #200
	CPY #100
	.byte SYSCALL, DUMP
		
	.byte SYSCALL, EXIT