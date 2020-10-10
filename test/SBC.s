; These are examples from "MCS6500 family programming manual" pp.15 ss.

; Pseudo-mnemonic for illegal instruction
; 6502bf will read next byte and perform a "SYStem CALL" accordingly
SYSCALL 	= $42	
DUMP 		= $00
PRINT 		= $10
READ 		= $11
PRINTSTR	= $12
EXIT		= $20

SEC
CLV
LDA #5
SBC #3
.byte SYSCALL, DUMP

SEC
CLV
LDA #5
SBC #6
.byte SYSCALL, DUMP

.byte SYSCALL, EXIT

