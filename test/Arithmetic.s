; Pseudo-mnemonic for illegal instruction
; 6502bf will read next byte and perform a "SYStem CALL" accordingly
SYSCALL 	= $42	
DUMP 		= $00
PRINT 		= $10
READ 		= $11
PRINTSTR	= $12
EXIT		= $20

		.byte SYSCALL, PRINTSTR, "ASL1", 13, 10, 0
		LDA #$81
		STA var
		CLC
		ASL var
		LDA var
		.byte SYSCALL, DUMP

		.byte SYSCALL, PRINTSTR, "ASL2", 13, 10, 0
		LDA #$81
		STA var
		SEC
		ASL var
		LDA var
		.byte SYSCALL, DUMP

		.byte SYSCALL, PRINTSTR, "LSR1", 13, 10, 0
		LDA #$81
		STA var
		CLC
		LSR var
		LDA var
		.byte SYSCALL, DUMP

		.byte SYSCALL, PRINTSTR, "LSR2", 13, 10, 0
		LDA #$81
		STA var
		SEC
		LSR var
		LDA var
		.byte SYSCALL, DUMP

		.byte SYSCALL, PRINTSTR, "ROL1", 13, 10, 0
		LDA #$81
		STA var
		CLC
		ROL var
		LDA var
		.byte SYSCALL, DUMP

		.byte SYSCALL, PRINTSTR, "ROL2", 13, 10, 0
		LDA #$81
		STA var
		SEC
		ROL var
		LDA var
		.byte SYSCALL, DUMP

		.byte SYSCALL, PRINTSTR, "ROR1", 13, 10, 0
		LDA #$81
		STA var
		CLC
		ROR var
		LDA var
		.byte SYSCALL, DUMP

		.byte SYSCALL, PRINTSTR, "ROR2", 13, 10, 0
		LDA #$81
		STA var
		SEC
		ROR var
		LDA var
		.byte SYSCALL, DUMP
		
		.byte SYSCALL, EXIT
	
var:	.byte 0