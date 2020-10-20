; Write chars from console
; It works only with stdin

.P02

.export         _write
.import         popax, popptr1
.importzp       ptr1

.include        "zeropage.inc"
.include        "6502bf.inc"

; int write (int fd, const void* buf, int count);
.proc   _write

	; push count in the 6502 stack
	pha		
	txa
	pha          		

	; push buf* into 6502 stack
	jsr     popptr1		; get buf
	lda ptr1
	pha
	lda ptr1+1
	pha

	jsr     popax       ; get fd and discard

	EMU_CC65_WRITE
	rts
.endproc
