; Interrupt handler.
;
; Checks for a BRK instruction and breaks from all valid interrupts.

.P02

.import   _exit
.export   _irq_int

.include "6502bf.inc"

.CODE

; ---------------------------------------------------------------------------
; Interrupt vectors

_irq_int:  
	TSX                    	; Transfer stack pointer to X
	INX						; Increment X so it points to the status register value saved on the stack
	LDA $100,X             	; Load status register contents
	AND #$10               	; Isolate B status bit
	BNE _break              ; If B = 1, BRK detected

	; IRQ detected, one should never jump here within the emulator

	EMU_PRINTLNS "*IRQ* detected."
	JMP _stop

_break:  
	; BRK detected, stop
	EMU_PRINTLNS "BRK detected."
			
_stop:
	EMU_DUMP
	EMU_QUIT             