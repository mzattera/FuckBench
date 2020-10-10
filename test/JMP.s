; Pseudo-mnemonic for illegal instruction
; 6502bf will read next byte and perform a "SYStem CALL" accordingly
SYSCALL 	= $42	
DUMP 		= $00
PRINT 		= $10
READ 		= $11
PRINTSTR	= $12
EXIT		= $20

		JMP lbl1
		
lbl2:	.byte SYSCALL, PRINTSTR, "Jumped to lbl2:", 13, 10, 0

; If address $3000 contains $40,
; $30FF contains $80,
; and $3100 contains $50,
; the result of JMP ($30FF) will be a transfer of control to $4080
; rather than $5080 as you intended

		LDA #>lbl3
		STA $3000
		LDA #<lbl3
		STA $30FF
		JMP ($30FF)

		.byte SYSCALL, PRINTSTR, "You should not be here!", 13, 10, 0
		.byte SYSCALL, EXIT
		
lbl1:	.byte SYSCALL, PRINTSTR, "Jumped to lbl1:", 13, 10, 0
		JMP lbl2

		.byte SYSCALL, PRINTSTR, "You should not be here!", 13, 10, 0
		.byte SYSCALL, EXIT
		
lbl3:	.byte SYSCALL, PRINTSTR, "Jumped to lbl3:", 13, 10, 0

; Test for infinite loop. In emulator this will cause an error and program to terminate.

here:	JMP here

		.byte SYSCALL, PRINTSTR, "You should not be here!", 13, 10, 0
		.byte SYSCALL, EXIT
