; implements clock() by reading 6502bf instruction counter.

.P02

.export         _clock
.import         popax, popsreg
.importzp 		sreg

.include        "6502bf.inc"

; clock_t clock (void);

.proc   _clock
	EMU_CC65_CLOCK
	
	; fetches return value from 6502 stack and put it in A/X/sreg
	pla
	sta sreg+1
	pla
	sta sreg
	pla
	tax
	pla
	
	rts
.endproc
