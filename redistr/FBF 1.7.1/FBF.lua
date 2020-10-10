-- Title: FuckBrainfuck (FBF) by Tritonio
-- Author: Asimakis Konstantinos. http://inshame.blogspot.com
-- Version: 1.7.1
--
-- Converts FBF source code to Brainfuck code. 
-- Reads source code from stdin and prints the resulting Brainfuck code to the stdout.
-- It also prints information messages to stderr.
--
-- Copyright (C) 2007  Asimakis Konstantinos
--
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License varsion 3 as 
-- published by the Free Software Foundation.
--
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with this program.  If not, see <http://www.gnu.org/licenses/>.

function gvn(wordy)
	if vn[string.upper(wordy)]~=nil then
		wordy=vn[string.upper(wordy)]
	else
		wordy=getvalueaftervariablecheck(wordy)
	end
	return wordy/1
end

function gstrictvn(wordy)
	errormessage="Unknown variable: "..wordy.."."
	wordy=vn[string.upper(wordy)]
	return wordy/1
end

function getstrictvalue(vall)
	errormessage="Wrong value: "..vall.."."
	if string.sub(vall,1,1)=="'" and string.sub(vall,3,3)=="'" then vall=string.byte(string.sub(vall,2,2)) end
	if string.upper(vall)=="TRUE" then vall=1 end
	if string.upper(vall)=="FALSE" then vall=0 end
	if math.floor(vall/1)~=vall/1 then error("kala milame gia poly megalo blaka.") end	
	return vall/1
end

function getvalueaftervariablecheck(vall)
	errormessage="Wrong value or unknown variable: "..vall.."."
	if string.sub(vall,1,1)=="'" and string.sub(vall,3,3)=="'" then vall=string.byte(string.sub(vall,2,2)) end
	if string.upper(vall)=="TRUE" then vall=1 end
	if string.upper(vall)=="FALSE" then vall=0 end	
	if math.floor(vall/1)~=vall/1 then error("kala milame gia megalo blaka.") end	
	return vall/1
end

function gisv(wordy)
	isitval=1
	if vn[string.upper(wordy)]~=nil then
	isitval=0
	end
	return isitval
end

function tols(coma)
	lss=lss+1
	ls[lss]=coma
end
function fromls()
	errormessage="Nothing to END."
	lss=lss-1
	return ls[lss+1]
end

function tocoms(coma)
	for i=1,comss do
		if word(coms[i],1)==word(coma,1) then errormessage="Recursion detected." error("At the moment of death I will smile; it's the triumph of shame and disease.") end
	end
	comss=comss+1
	coms[comss]=coma
end
function fromcoms()
	comss=comss-1
	return coms[comss+1]
end

function set(pointer,value)
zero(pointer)
inc(pointer,value)
end

function inline(code)
	for i=1,string.len(code) do
	if string.sub(code,i,i)=="+" then
			addplus(1)
		end		
		if string.sub(code,i,i)=="-" then
			addminus(1)
		end
		if string.sub(code,i,i)=="<" then
			addleft(1)
		end
		if string.sub(code,i,i)==">" then
			addright(1)
		end
		if string.sub(code,i,i)=="]" then
			addclose(1)
		end
		if string.sub(code,i,i)=="[" then
			addopen(1)
		end
		if string.sub(code,i,i)~="]" and string.sub(code,i,i)~="[" and string.sub(code,i,i)~="-" and string.sub(code,i,i)~="+" and string.sub(code,i,i)~="<" and string.sub(code,i,i)~=">" then
			out=out..string.sub(code,i,i)
		end
	end
end

function msg(text) --0
	for i=1,string.len(text) do
		inc(0,string.byte(string.sub(text,i,i)))
		outp(0)
		dec(0,string.byte(string.sub(text,i,i)))
	end
end

function comp(pointer1,isv1,pointer2,isv2,pointer3) --0,1,2,3
	if isv1==0 then copy(pointer1,0) else set(0,pointer1) end
	if isv2==0 then copy(pointer2,1) else set(1,pointer2) end
	inc(3,1)
	inc(2,1)
	Suneq(3,0,1)
	Sifeq(0,0,1)
	inc(2,1)
	set(3,0)
	Eifeq(0,0,1)
	Sifeq(1,0,1)
	dec(2,1)
	set(3,0)
	Eifeq(1,0,1)
	dec(0,1)
	dec(1,1)
	Euneq(3,0,1)
	zero(0)
	zero(1)
	move(2,pointer3)
end

function wt(pointer,index,isv1,value,isv2) --none
	if isv1==0 then copy(index,pointer+4) else inc(pointer+4,index) end --itan set
	if isv2==0 then copy(value,pointer+2) else inc(pointer+2,value) end --itan set
	addright(pointer)
	inline("+>>>>[-[->>+<<]<<[->>+<<]>>>>]")
	inline("<[-]<[->+<]")
	inline("<<-[+<<-]")
	addleft(pointer)
end

function rt(pointer,index,isv1,variable) --none
	if isv1==0 then copy(index,pointer+4) else inc(pointer+4,index) end --itan set
	addright(pointer)
	inline("+>>>>[-[->>+<<]>>]")
	inline("<[->+<<+>]>[-<+>]")
	inline("<<<<-[+>>[-<<+>>]<<<<-]")
	addleft(pointer)
	move(pointer+2,variable)
end

function push(pointer,value,isv2) --none
	copy(pointer+1,pointer+4)
	inc(pointer+1,1)
	if isv2==0 then copy(value,pointer+2) else inc(pointer+2,value) end --itan set
	addright(pointer)
	inline("+>>>>[-[->>+<<]<<[->>+<<]>>>>]")
	inline("<[-]<[->+<]")
	inline("<<-[+<<-]")
	addleft(pointer)
end

function pop(pointer,variable) --none
	dec(pointer+1,1)
	copy(pointer+1,pointer+4)
	addright(pointer)
	inline("+>>>>[-[->>+<<]>>]")
	inline("<[->+<<+>]>[-<+>]")
	inline("<<<<-[+>>[-<<+>>]<<<<-]")
	addleft(pointer)
	move(pointer+2,variable)
end

function zero(pointer) --none
	addright(pointer)
	while string.sub(out,-1,-1)=="+" or string.sub(out,-1,-1)=="-" do out=string.sub(out,1,-2) end
	if string.sub(out,-1)~="]" then out=out.."[-]" end
	addleft(pointer)
end

function addminus(value) --none
	while  (string.sub(out,-1)=="+" and value>0) do value=value-1 out=string.sub(out,1,-2) end
	for i=1,value do out=out.."-" end
end

function addright(value) --none
	while  (string.sub(out,-1)=="<" and value>0) do value=value-1 out=string.sub(out,1,-2) end
	for i=1,value do out=out..">" end
end

function addleft(value) --none
	while  (string.sub(out,-1)==">" and value>0) do value=value-1 out=string.sub(out,1,-2) end
	for i=1,value do out=out.."<" end
end

function addplus(value) --none
	while (string.sub(out,-1)=="-" and value>0) do value=value-1 out=string.sub(out,1,-2) end
	for i=1,value do out=out.."+" end
end

function addopen() --none
	out=out.."["
end

function addclose() --none
	if string.sub(out,-1)=="[" then out=string.sub(out,1,-2) else out=out.."]" end
end

function Suneq(pointer,pointer2,isval2) --none
	if isval2==0 then
		sub(pointer,0,pointer2,0,pointer)
		addright(pointer)
	else
		addright(pointer)
		addminus(pointer2)
	end
	addopen()
	if isval2==0 then
		addleft(pointer)
		add(pointer,0,pointer2,0,pointer)
	else
		addplus(pointer2)
		addleft(pointer)
	end
end

function Euneq(pointer,pointer2,isval2) --none
	if isval2==0 then
		sub(pointer,0,pointer2,0,pointer)
		addright(pointer)
	else
		addright(pointer)
		addminus(pointer2)
	end
	addclose()
	if isval2==0 then
		addleft(pointer)
		add(pointer,0,pointer2,0,pointer)
	else
		addplus(pointer2)
		addleft(pointer)
	end
end

function Sifnoteq(pointer,pointer2,isval2) --none
	copy(pointer,4)
	Suneq(pointer,pointer2,isval2)
end

function Eifnoteq(pointer,pointer2,isval2) --4 
	move(pointer,4)
	if isval2==0 then
		copy(pointer2,pointer)
	else
		inc(pointer,pointer2) --anti gia set
	end
	Euneq(pointer,pointer2,isval2)
	move(4,pointer)
end

function Sifeq(pointer,pointer2,isval2) --5
	Sifnoteq(pointer,pointer2,isval2)
	inc(5,1)		   --anti gia set
	Eifnoteq(pointer,pointer2,isval2)
	Sifnoteq(5,1,1)
end

function Eifeq(pointer,pointer2,isval2) --5
	Eifnoteq(5,1,1)
	zero(5)
end

function inc(pointer,value) --none
	addright(pointer)
	addplus(value)
	addleft(pointer)
end

function dec(pointer,value) --none
	addright(pointer)
	addminus(value)
	addleft(pointer)
end

function add(pointer1,isv1,pointer2,isv2,pointer3) --0,1,2
	if isv1==0 then copy(pointer1,0) else inc(0,pointer1) end --anti gia set
	if isv2==0 then copy(pointer2,1) else inc(1,pointer2) end --anti gia set
	Suneq(0,0,1)
	inc(1,1)
	dec(0,1)
	Euneq(0,0,1)
	move(1,pointer3)
end

function move(pointer1,pointer2) --none!
	zero(pointer2)
	Suneq(pointer1,0,1)
	dec(pointer1,1)
	inc(pointer2,1)
	Euneq(pointer1,0,1)
end

function sub(pointer1,isv1,pointer2,isv2,pointer3) --0,1,2
	if isv1==0 then copy(pointer1,0) else inc(0,pointer1) end --anti gia set
	if isv2==0 then copy(pointer2,1) else inc(1,pointer2) end --anti gia set
	Suneq(1,0,1)
	dec(0,1)
	dec(1,1)
	Euneq(1,0,1)
	move(0,pointer3)
end

function multi(pointer1,isv1,pointer2,isv2,pointer3) --0,1,2
	if isv1==0 then copy(pointer1,0) else inc(0,pointer1) end --anti gia set
	Suneq(0,0,1)
	if isv2==0 then copy(pointer2,1) else inc(1,pointer2) end --anti gia set
	Suneq(1,0,1)
	dec(1,1)
	inc(2,1)
	Euneq(1,0,1)
	dec(0,1)
	Euneq(0,0,1)
	move(2,pointer3)
end

function div(pointer1,isv1,pointer2,isv2,pointer3) --0,1,2
	if isv1==0 then copy(pointer1,0) else inc(0,pointer1) end --anti gia set
	Suneq(0,0,1)
	if isv2==0 then copy(pointer2,1) else inc(1,pointer2) end --anti gia set
	Suneq(1,0,1)
	dec(0,1)
	dec(1,1)
	Sifeq(0,0,1)
	Sifnoteq(1,0,1)
	dec(2,1)
	zero(1)
	Eifnoteq(1,0,1)
	Eifeq(0,0,1)
	Euneq(1,0,1)
	inc(2,1)
	Euneq(0,0,1)
	move(2,pointer3)
end

function mod(pointer1,isv1,pointer2,isv2,pointer3) --3
	div(pointer1,isv1,pointer2,isv2,3)
	multi(3,0,pointer2,isv2,3)
	sub(pointer1,isv1,3,0,pointer3)
	zero(3)
end

function a2b(pointer1,isv1,pointer2,isv2,pointer3,isv3,pointer4) --7
	if isv1==0 then copy(pointer1,7) else inc(7,pointer1) end dec(7,48)
	multi(7,0,10,1,7)
	add(pointer2,isv2,7,0,7)
	dec(7,48)
	multi(7,0,10,1,7)
	add(pointer3,isv3,7,0,7)
	dec(7,48)
	move(7,pointer4)
end

function b2a(pointer1,isv1,pointer2,pointer3,pointer4) --7
	if isv1==0 then copy(pointer1,7) else inc(7,pointer1) end
	mod(7,0,10,1,pointer4)
	inc(pointer4,48)
	div(7,0,10,1,7)
	mod(7,0,10,1,pointer3)
	inc(pointer3,48)
	div(7,0,10,1,7)
	move(7,pointer2)
	inc(pointer2,48)
end

function copy(pointer1,pointer2) --6
	zero(pointer2)
	Suneq(pointer1,0,1)
	dec(pointer1,1)
	inc(pointer2,1)
	inc(6,1)
	Euneq(pointer1,0,1)
	Suneq(6,0,1)
	dec(6,1)
	inc(pointer1,1)
	Euneq(6,0,1)
end

function inp(pointer) --none
	addright(pointer)
	out=out..","
	if echo then out=out.."." end
	addleft(pointer)
end

function outp(pointer) --none
	addright(pointer)
	out=out.."."
	addleft(pointer)
end

function trim(str)
	return(string.gsub(str,"^%s*(.-)%s*$", "%1"))
end

function word(text,number) --none
	errormessage="Wrong number of arguments."
	text=text.." "
	start=1 fin=string.find(text," ",start)-1
	for i=2,number do
		start,fin=fin+2,string.find(text," ",fin+2)-1
	end
	return string.sub(text,start,fin)
end

function wordcount(text) --none
	curword=2
	totwords=0
	while pcall(word,text,curword)==true do
		curword=curword+1
		totwords=totwords+1
	end
	return totwords
end

function wordall(text,number) --none
	errormessage="Wrong number of arguments."
	text=text.." "
	start=string.find(text," ",start)+1
	return string.sub(text,start,-2)
end

function recog()
	collectgarbage("collect")
	local found=false
	if word(string.upper(com),1)=="#ENDBLOCK" then found=true curblock=0 end
	if curblock~=0 then found=true curblockcom=curblockcom+1 blocks[curblock][curblockcom]=com else
		if word(string.upper(com),1)=="END" then com="E"..fromls() end
		if word(string.upper(com),1)=="SET" then found=true set(gstrictvn(word(com,2)),getstrictvalue(word(com,3))) end
		if word(string.upper(com),1)=="UNEQ" then found=true Suneq(gstrictvn(word(com,2)),gvn(word(com,3)),gisv(word(com,3))) tols(com) end
		if word(string.upper(com),1)=="EUNEQ" then found=true Euneq(gstrictvn(word(com,2)),gvn(word(com,3)),gisv(word(com,3))) end
		if word(string.upper(com),1)=="IFEQ" then found=true Sifeq(gstrictvn(word(com,2)),gvn(word(com,3)),gisv(word(com,3))) tols(com) end
		if word(string.upper(com),1)=="EIFEQ" then found=true Eifeq(gstrictvn(word(com,2)),gvn(word(com,3)),gisv(word(com,3))) end
		if word(string.upper(com),1)=="IFNOTEQ" then found=true Sifnoteq(gstrictvn(word(com,2)),gvn(word(com,3)),gisv(word(com,3))) tols(com) end
		if word(string.upper(com),1)=="EIFNOTEQ" then found=true Eifnoteq(gstrictvn(word(com,2)),gvn(word(com,3)),gisv(word(com,3))) end
		if word(string.upper(com),1)=="INC" then found=true inc(gstrictvn(word(com,2)),getstrictvalue(word(com,3))) end
		if word(string.upper(com),1)=="DEC" then found=true dec(gstrictvn(word(com,2)),getstrictvalue(word(com,3))) end
		if word(string.upper(com),1)=="ADD" then found=true add(gvn(word(com,2)),gisv(word(com,2)),gvn(word(com,3)),gisv(word(com,3)),gstrictvn(word(com,4))) end
		if word(string.upper(com),1)=="ASCII2BYTE" then found=true a2b(gvn(word(com,2)),gisv(word(com,2)),gvn(word(com,3)),gisv(word(com,3)),gvn(word(com,4)),gisv(word(com,4)),gstrictvn(word(com,5))) end
		if word(string.upper(com),1)=="BYTE2ASCII" then found=true b2a(gvn(word(com,2)),gisv(word(com,2)),gstrictvn(word(com,3)),gstrictvn(word(com,4)),gstrictvn(word(com,5))) end
		if word(string.upper(com),1)=="SUB" then found=true sub(gvn(word(com,2)),gisv(word(com,2)),gvn(word(com,3)),gisv(word(com,3)),gstrictvn(word(com,4))) end
		if word(string.upper(com),1)=="MULTI" then found=true multi(gvn(word(com,2)),gisv(word(com,2)),gvn(word(com,3)),gisv(word(com,3)),gstrictvn(word(com,4))) end
		if word(string.upper(com),1)=="DIV" then found=true div(gvn(word(com,2)),gisv(word(com,2)),gvn(word(com,3)),gisv(word(com,3)),gstrictvn(word(com,4))) end
		if word(string.upper(com),1)=="COMP" then found=true comp(gvn(word(com,2)),gisv(word(com,2)),gvn(word(com,3)),gisv(word(com,3)),gstrictvn(word(com,4))) end
		if word(string.upper(com),1)=="MOD" then found=true mod(gvn(word(com,2)),gisv(word(com,2)),gvn(word(com,3)),gisv(word(com,3)),gstrictvn(word(com,4))) end
		if word(string.upper(com),1)=="COPY" then found=true copy(gstrictvn(word(com,2)),gstrictvn(word(com,3))) end
		if word(string.upper(com),1)=="COPYSIZE" then found=true copy(gstrictvn(word(com,2))+1,gstrictvn(word(com,3))) end
		if word(string.upper(com),1)=="READ" then found=true for i=1,wordcount(com) do inp(gstrictvn(word(com,i+1))) end end
		if word(string.upper(com),1)=="PRINT" then found=true for i=1,wordcount(com) do outp(gstrictvn(word(com,i+1))) end end
		if word(string.upper(com),1)=="MSG" then found=true msg(wordall(com,2)) end
		if word(string.upper(com),1)=="LINE" then found=true msg(linebreaker) end
		if word(string.upper(com),1)=="TAB" then found=true msg(string.char(9)) end
		if word(string.upper(com),1)=="BEEP" then found=true msg(string.char(7)) end
		if word(string.upper(com),1)=="MSGCLEAR" then found=true msg(string.rep(string.char(8),getstrictvalue(word(com,2)))..string.rep(" ",getstrictvalue(word(com,2)))..string.rep(string.char(8),getstrictvalue(word(com,2)))) end
		if word(string.upper(com),1)=="SPACE" then found=true msg(" ") end
		if word(string.upper(com),1)=="#ECHO" then found=true echo=true end
		if word(string.upper(com),1)=="#BYTECELLS" then found=true assume256=true end
		if word(string.upper(com),1)=="#DIM" then found=true for i=1,wordcount(com) do cv=cv+1 if vn[word(string.upper(com),i+1)]~=nil then errormessage="Variable "..word(com,i+1).." has already been declared." error("Mataiotis mataiotiton ta panta mataiotis...") end variablelog=variablelog..cv.." : "..word(com,i+1).."\n" vn[word(string.upper(com),i+1)]=cv end end
		if word(string.upper(com),1)=="#TABLE" then found=true cv=cv+1 variablelog=variablelog..cv.."-" vn[word(string.upper(com),2)]=cv cv=cv+getstrictvalue(word(com,3))*2+2 variablelog=variablelog..cv.." : "..word(com,2).."\n" end
		if word(string.upper(com),1)=="#CUSTOM" then found=true variablelog=variablelog..cv+1 .."-" cv=cv+getstrictvalue(word(com,2)) variablelog=variablelog..cv.." : CUSTOM SPACE\n" end
		if word(string.upper(com),1)=="RTABLE" then found=true rt(gstrictvn(word(com,2)),gvn(word(com,3)),gisv(word(com,3)),gstrictvn(word(com,4))) end
		if word(string.upper(com),1)=="WTABLE" then found=true wt(gstrictvn(word(com,2)),gvn(word(com,3)),gisv(word(com,3)),gvn(word(com,4)),gisv(word(com,4))) end
		if word(string.upper(com),1)=="POP" then found=true pop(gstrictvn(word(com,2)),gstrictvn(word(com,3))) end
		if word(string.upper(com),1)=="PUSH" then found=true push(gstrictvn(word(com,2)),gvn(word(com,3)),gisv(word(com,3))) end
		if word(string.upper(com),1)=="MOVETO" then found=true addright(gvn(word(com,2))) end
		if word(string.upper(com),1)=="RETURNFROM" then found=true addleft(gvn(word(com,2))) end
		if word(string.upper(com),1)=="BRAINFUCK" then found=true inline(wordall(com,2)) end
		if word(string.upper(com),1)=="REM" then found=true end
		if word(string.upper(com),1)=="--" then found=true end
		if word(string.upper(com),1)=="//" then found=true end
		if word(string.upper(com),1)=="#LINEBREAKS" then found=true linebreaks=getstrictvalue(word(com,2)) end
		if word(string.upper(com),1)=="#LINEMODE" then found=true if string.upper(word(com,2))=="DOS" then linebreaker=string.char(13)..string.char(10) end if string.upper(word(com,2))=="LINUX" then linebreaker=string.char(10) end if string.upper(word(com,2))=="MAC" then linebreaker=string.char(13) end end
		if not found and blockvars[string.upper(word(com,1))]~=nil then found=true
			for i=1,blockvars[string.upper(word(com,1))][0] do
				vn[blockvars[string.upper(word(com,1))][i]]=gstrictvn(string.upper(word(com,i+1)))
			end
			tocoms(com)
			for i,subcom in ipairs(blocks[string.upper(word(com,1))]) do
				com=subcom
				io.stderr:write("Processing: In block "..string.upper(word(coms[comss],1))..string.char(9).." Depth: "..lss..string.char(9)..com.."\n")
				if DEBUG then
					recog()
				else	
					if pcall(recog)==false then
						if not errorprinted then
							huston_weve_got_a_problem=true
							io.stderr:write("\nERROR IN CODE LINE: "..com.."\n")
							io.stderr:write(errormessage.."\n")
							errorprinted=true
						end
						error("I used to be a break")
					end
				end
			end
			com=fromcoms()
			for i=1,blockvars[string.upper(word(com,1))][0] do
				vn[blockvars[string.upper(word(com,1))][i]]=nil
			end
		end
		if word(string.upper(com),1)=="#BLOCK" then
			found=true
			curblock=string.upper(word(com,2))
			curblockcom=0
			blocks[curblock]={}
			blockvars[curblock]={}
			blockvars[curblock][0]=wordcount(com)-1
			for i=1,wordcount(com)-1 do
				blockvars[curblock][i]=string.upper(word(com,i+2)) 
			end
		end
	end
	if not found and com~="" then errormessage="Unknown command or instruction." error("Bas bas bas... O paraskeyas bas bas...") end
end

io.stderr:write("\nFuckBrainfuck Compiler v1.7.1 by Tritonio\n") 
io.stderr:write("http://inshame.blogspot.com\n")
io.stderr:write("Copyright (C) 2007  Asimakis Konstantinos\n\n")
io.stderr:write("This program is licensed under the GNU/GPL\n")
io.stderr:write("version 3 and is distributed without any\n")
io.stderr:write("warranty. Make sure you have read the\n")
io.stderr:write("'FBF LICENSE.txt' file. You can find a copy of\n")
io.stderr:write("the license here: http://www.gnu.org/licenses/\n\n")
io.stderr:write("Reading source code from stdin.\n") 


DEBUG=false
cv=7
cl=0
ls={}
lss=0
coms={}
variablelog="0-7 : Reserved\n"
comss=0
echo=false
assume256=false
vn={}
blocks={}
blockvars={}
linebreaker=string.char(10)
curblock=0
inblock=0
huston_weve_got_a_problem=false
errorprinted=false
out=""
com=io.read()
while com~=nil and huston_weve_got_a_problem==false do
	cl=cl+1
	com=trim(com) --tin trimarei afoy exei sigoyreytei oti den einai nil.
	io.stderr:write("Processing: Line "..cl..string.char(9).." Depth: "..lss..string.char(9)..com.."\n")	
	if DEBUG then
		recog()
	else	
		if pcall(recog)==false then
			if not errorprinted then
				huston_weve_got_a_problem=true
				io.stderr:write("\nERROR IN CODE LINE: "..com.."\n")
				io.stderr:write(errormessage.."\n")
				errorprinted=true
			end
		end
	end
	com=io.read()
end

io.stderr:write("\n")

if not huston_weve_got_a_problem then
	if lss>0 or curblock~=0 then
		com="" io.stderr:write("ERROR: Unclosed block or structure!\n\n")
		io.stderr:write("Happy debugging...\n")
	else
		io.stderr:write("This program uses "..cv+1 .." cells.\n\n")
		io.stderr:write("Memory map:\n"..variablelog.."\n")
		while string.sub(out,-1,-1)=="+" or string.sub(out,-1,-1)=="-" or string.sub(out,-1,-1)==">" or string.sub(out,-1,-1)=="<" do
			out=string.sub(out,1,-2)
		end
		if assume256 then
			for i=256,129,-1 do
				out=string.gsub(out,string.rep("%+",i),string.rep("-",256-i))
				out=string.gsub(out,string.rep("%-",i),string.rep("+",256-i))
			end
		end
		io.stderr:write("The source code has been successfully compiled!\n\nWriting "..string.len(out).." Brainfuck commands to stdout")
		if linebreaks~=nil then io.stderr:write(" with line breaks every "..linebreaks.." characters") end
		io.stderr:write("...\n")
		if linebreaks~=nil then
			for i=1,string.len(out),linebreaks do print(string.sub(out,i,i+linebreaks-1)) end
		else
			print(out)
		end
		io.stderr:write("The Brainfuck code has been written to stdout.\n")
		io.stderr:write("Have fun...\n")
	end
else
io.stderr:write("Happy debugging...\n")
end
