; Pseudo-mnemonic for illegal instruction
; 6502bf will read next byte and perform a "SYStem CALL" accordingly
SYSCALL 	= $42	
DUMP 		= $00
PRINT 		= $10
READ 		= $11
PRINTSTR	= $12
EXIT		= $20
		LDA #5
		STA pippo
		INC pippo
		LDA	pippo
		.byte SYSCALL, DUMP

		LDA #0
		STA pippo
		INC pippo
		LDA	pippo
		.byte SYSCALL, DUMP

		LDA #255
		STA pippo
		INC pippo
		LDA	pippo
		.byte SYSCALL, DUMP

		LDA #5
		STA pippo
		DEC pippo
		LDA	pippo
		.byte SYSCALL, DUMP

		LDA #0
		STA pippo
		DEC pippo
		LDA	pippo
		.byte SYSCALL, DUMP

		LDA #255
		STA pippo
		DEC pippo
		LDA	pippo
		.byte SYSCALL, DUMP

		.byte SYSCALL, EXIT
		
pippo:	.byte 0

