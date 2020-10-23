; Read 6502bf instruction counter (surrogate for real time clock).

.P02

.export         _get_instr_count
.import         popax, popptr1
.importzp       ptr1

.include        "6502bf.inc"

; void __fastcall__ get_instr_count(unsigned long*);

.proc   _get_instr_count
	EMU_CC65_CLOCK
	rts
.endproc

