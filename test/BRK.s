; Pseudo-mnemonic for illegal instruction
; 6502bf will read next byte and perform a "SYStem CALL" accordingly
SYSCALL 	= $42	
DUMP 		= $00
PRINT 		= $10
READ 		= $11
PRINTSTR	= $12
EXIT		= $20
		
		; Set interrupt vector for brk
		LDA #<intr
		STA $FFE6
		LDA #>intr
		STA $FFE7
		
		.byte SYSCALL, DUMP
		BRK
		NOP	; Used as padding for BRK code that ca65 does not add itself
		
		.byte SYSCALL, PRINTSTR, "Returned from interrupt.", 13, 10, 0
		.byte SYSCALL, DUMP
		
		.byte SYSCALL, EXIT
		
intr:	.byte SYSCALL, PRINTSTR, "Entered interrupt routine.", 13, 10, 0
		RTI
		
		.byte SYSCALL, PRINTSTR, "You should not be here.", 13, 10, 0
		.byte SYSCALL, EXIT