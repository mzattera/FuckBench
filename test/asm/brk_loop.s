; Tests infinite BRK loop detection (if BRK causes jumping to same location exists)

.P02

; Useful macros
.include        "..\..\cc65\6502bf.inc"

; ---------------------------------------------------------------------------
; A little light 6502 housekeeping

	LDX     #$FF                 ; Initialize stack pointer to $01FF
	TXS
	CLD                          ; Clear decimal mode



; ---------------------------------------------------------------------------
; Main code

	.byte $00
