; Pseudo-mnemonic for illegal instruction
; 6502bf will read next byte and perform a "SYStem CALL" accordingly
SYSCALL 	= $42	
DUMP 		= $00
PRINT 		= $10
READ 		= $11
PRINTSTR	= $12
EXIT		= $20
		
		CLC
		BCC lbl1
		
lbl2:	.byte SYSCALL, PRINTSTR, "BRANCH ERROR!!!", 13, 10, 0

lbl3:	.byte SYSCALL, PRINTSTR, "Jumped to lbl3:", 13, 10, 0

		CLV
		BVS lbl4
		.byte SYSCALL, PRINTSTR, "Negative check OK", 13, 10, 0
		
		BVC *
		
		.byte SYSCALL, PRINTSTR, "You shold not be here.", 13, 10, 0
		.byte SYSCALL, EXIT

lbl1:	.byte SYSCALL, PRINTSTR, "Jumped to lbl1:", 13, 10, 0
		SEC
		BCS lbl3
		
lbl4:	.byte SYSCALL, PRINTSTR, "BRANCH ERROR!!!", 13, 10, 0
		
		.byte SYSCALL, EXIT