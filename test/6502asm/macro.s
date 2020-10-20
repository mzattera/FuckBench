; Tests for macros in 6502bf.inc

.P02

; Useful macros
.include        "..\..\cc65\6502bf.inc"

.SEGMENT "ZEROPAGE"

zpbyt:
	.byte $00
zpwrd:
	.word $0000


.SEGMENT "DATA"

str:
	.byte "ZZZ", 00
byt:
	.byte $12
wrd:
	.word $3456
	
.SEGMENT "CODE"
; ---------------------------------------------------------------------------
; A little light 6502 housekeeping

	LDX     #$FF                 ; Initialize stack pointer to $01FF
	TXS
	CLD                          ; Clear decimal mode

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

	lda #$AB
	sta zpbyt
	
	lda #$EF
	sta zpwrd
	lda #$CD
	sta zpwrd+1
	
	EMU_DUMP
	
	lda #$7C
	EMU_PUTC
	lda #65
	EMU_PUTC
	lda #$7C
	EMU_PUTC

	EMU_PRINTS "XXX"
	lda #$7C
	EMU_PUTC

	EMU_PRINTLNS "YYY"
	lda #$7C
	EMU_PUTC
	
	EMU_PRINTSAT str  
	lda #$7C
	EMU_PUTC
	
	EMU_PRINTBAT byt
	lda #$7C
	EMU_PUTC
	
	EMU_PRINTWAT wrd
	lda #$7C
	EMU_PUTC
	
	EMU_PRINTBZP zpbyt
	lda #$7C
	EMU_PUTC
	
	EMU_PRINTWZP zpwrd
	lda #$7C
	EMU_PUTC
	
	EMU_QUIT

	

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