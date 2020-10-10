; Pseudo-mnemonic for illegal instruction
; 6502bf will read next byte and perform a "SYStem CALL" accordingly
SYSCALL 	= $42	
DUMP 		= $00
PRINT 		= $10
READ 		= $11
PRINTSTR	= $12
EXIT		= $20
	

; In indirect indexed addressing (referred to as (Indirect), Y), the second byte
; of the instruction points to a memory location in page zero. The contents of this memory location
; is added to the contents of the Y index register, the result being the low order eight bits of the
; effective address. The carry from this addition is added to the contents of the next page zero
; memory location, the result being the high order eight bits of the effective address.

		lda data
		.byte SYSCALL, DUMP
		
		lda #$33
		.byte SYSCALL, DUMP
		
		lda #<data
		sta $10
		lda #>data
		sta $11
		ldy #0
		lda ($10),y
		.byte SYSCALL, DUMP
		
		.byte SYSCALL, EXIT

data:	.byte $66
	
	