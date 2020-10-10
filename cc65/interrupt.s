; Interrupt handler.
;
; Checks for a BRK instruction and breaks from all valid interrupts.

.P02

.import   _exit
.export   _irq_int

.include "6502bf.inc"

.CODE

; ---------------------------------------------------------------------------
; Maskable interrupt (IRQ) service routine

_irq_int:  
		   TSX                    ; Transfer stack pointer to X
           LDA $100,X             ; Load status register contents
           AND #$10               ; Isolate B status bit
           BNE break              ; If B = 1, BRK detected

; ---------------------------------------------------------------------------
; IRQ detected, return
; One should never jump here within the emulator

irq:   		EMU_PRINTLNS "*IRQ* detected!"
			JMP stop

; ---------------------------------------------------------------------------
; BRK detected, stop

break:     	EMU_PRINTLNS "BRK detected."
			
stop:		EMU_DUMP
			EMU_QUIT             