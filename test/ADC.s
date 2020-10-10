; These are examples from "MCS6500 family programming manual" pp.7 ss.

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
LDA #13
ADC #211
.byte SYSCALL, DUMP

SEC
CLV
LDA #254
ADC #6
.byte SYSCALL, DUMP

CLC
CLV
LDA #5
ADC #7
.byte SYSCALL, DUMP

CLC
CLV
LDA #127
ADC #2
.byte SYSCALL, DUMP

CLC
CLV
LDA #5
ADC #$FD ; -3
.byte SYSCALL, DUMP

CLC
CLV
LDA #5
ADC #$F9 ; -7
.byte SYSCALL, DUMP

CLC
CLV
LDA #$FB ; -5
ADC #$F9 ; -7
.byte SYSCALL, DUMP

CLC
CLV
LDA #$BE ; -66
ADC #$BF ; -65
.byte SYSCALL, DUMP

.byte SYSCALL, EXIT
