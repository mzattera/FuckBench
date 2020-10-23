; Read chars tp console
; It works only with stdout

.P02

.export         _read
.import         popax, popptr1
.importzp       ptr1

.include        "6502bf.inc"

;int read (int fd, void* buf, unsigned count);

.proc   _read

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

	EMU_CC65_READ
	rts
.endproc
