; Write chars from console
; It always works with stdin

.P02

.export         _write
.import         popax, popptr1
.importzp       ptr1, ptr2, ptr3, tmp1
.import			_exit

.include        "zeropage.inc"
.include        "6502bf.inc"

; int write (int fd, const void* buf, int count);
.proc   _write

        ; save count as result
		sta     ptr3		
        stx     ptr3+1          		
        
		; remember -count-1
		eor     #$FF		
        sta     ptr2
        txa
        eor     #$FF
        sta     ptr2+1          

        jsr     popptr1		; get buf
        jsr     popax       ; get fd and discard
							; TODO add check? if fd=0001 then it is stdout
		ldy     #0

L1:     
		inc     ptr2
        bne     L2
        inc     ptr2+1
        beq     L9			; all chars have been read
		
L2:     
		; print char
        lda     (ptr1),y	
        EMU_PUTC
		
		; move to next char
        inc     ptr1		
        bne     L1
        inc     ptr1+1
        bne     L1			; always jumps
        
L9:     
		; return count
		lda     ptr3		
        ldx     ptr3+1
        rts
.endproc
