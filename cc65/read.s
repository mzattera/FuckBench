; Read chars tp console
; It always works with stdout

.P02

.export         _read
.import         popax, popptr1
.importzp       ptr1, ptr2, ptr3

.include        "zeropage.inc"
.include        "6502bf.inc"

;int read (int fd, void* buf, unsigned count);

.proc   _read
		; Store -count-1 in ptr 2
        eor     #$FF	
        sta     ptr2
        txa
        eor     #$FF
        sta     ptr2+1    

		jsr     popptr1	; pointer to buf in ptr1
				
		; stores initial buffer location in ptr3
		lda		ptr1	
		sta		ptr3
		lda		ptr1+1
		sta		ptr3+1
		
		jsr     popax   ; get fd and discard it.
						; TODO add check? If fd==1 then it is stdin

		ldy     #0		; zp index

L1:		
		; If we read enough chars, return
	    inc     ptr2
        bne     L2
        inc     ptr2+1
        beq     L5

L2:
		EMU_GETC		; Read char in accumulator
		bne		L3
		lda		#$0A	; replace $00 (nul) with $0A (LF)

L3:
        sta     (ptr1),y
        
		; increment pointer to buffer
        inc     ptr1	
        bne     L4
        inc     ptr1+1

L4: 
		cmp		#$0A
		bne		L1	; exit on newline (LF)
				
L5:		
		;  compute and returns # of bytes read
		lda     ptr1+1
	    sec
		sbc     ptr3+1
		tax
		lda     ptr1
		sec
		sbc     ptr3
		rts
.endproc
