; Checks that using decimal mode causes an error

.P02

; Useful macros
.include        "..\..\cc65\6502bf.inc"

; ---------------------------------------------------------------------------
; A little light 6502 housekeeping

	LDX     #$FF                 ; Initialize stack pointer to $01FF
	TXS
	SED                          ; Set decimal mode

; ---------------------------------------------------------------------------
; Set interrupt vectors
; IRQ/BRK	FFFE	FFFF 
; NMI		FFFA	FFFB ; this can be ignored, as no interrups are generated in code 
; RESET		FFFC	FFFD ; this is set up at emulator start by the emu itself

	LDA     #<_irq_int			 
	STA     $FFFE
	LDA     #>_irq_int
	STA     $FFFF



; ---------------------------------------------------------------------------
; Main code

	lda	#01
	adc #01

	

; ---------------------------------------------------------------------------
; Interrupt vectors

_irq_int:  
	TSX                    ; Transfer stack pointer to X
	LDA $100,X             ; Load status register contents
	AND #$10               ; Isolate B status bit
	BNE _break              ; If B = 1, BRK detected

	; IRQ detected, one should never jump here within the emulator

	EMU_PRINTLNS "*IRQ* detected!"
	JMP _stop

_break:  
	; BRK detected, stop
	EMU_PRINTLNS "BRK detected."
			
_stop:
	EMU_DUMP
	EMU_QUIT             