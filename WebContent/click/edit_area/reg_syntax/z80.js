editAreaLoader.load_syntax["z80"] = {
	'COMMENT_SINGLE' : { }
	,'COMMENT_MULTI' : { }
	,'QUOTEMARKS' : { }
	,'KEYWORD_CASE_SENSITIVE' : false
	,'KEYWORDS' : {
		'statements' : [
			'LD' ,	'EX' ,	'EXX' ,	'PUSH' ,
			'POP' ,	'LDI' ,	'LDD' ,	'LDIR' ,
			'LDDR' ,'CPI' ,	'CPD' ,	'CPIR' ,
			'CPDR' ,'MULUB' ,'MULUW' ,'ADD' ,
			'ADC' ,	'INC' ,	'SUB' ,	'SBC' ,
			'DEC' ,	'CP' ,	'AND' ,	'OR' ,
			'XOR' ,	'BIT' ,	'SET' ,	'RES' ,
			'RLCA' ,'RRCA' ,'RLA' ,'RRA' ,
			'RLC' ,'RRC' ,'RL' ,'RR' ,
			'RLD' ,'RRD' ,'SLL' ,'SLA' ,
			'SRL' ,'SRA' ,'JP' ,'JR' ,
			'DJNZ' ,'CALL' ,'RET' ,'RST' ,
			'IN' ,	'INI' ,	'IND' ,	'INIR' ,
			'INDR' ,'OUT' ,	'OUTI' ,'OUTD' ,
			'OTIR' ,'OTDR' ,'DAA' ,	'CPL' ,
			'NEG' ,	'CCF' ,	'SCF' ,	'NOP' ,
			'HALT' ,'DI' ,	'EI' ,	'IM' 
		]
		,'keywords' : [
				'B' ,	'C' ,	'D' ,	'E' ,
				'H' ,	'L' ,	'A' ,	'IXH' ,
				'IXL' ,	'IYH' ,	'IYL' ,	'I' ,
				'R' ,	'SP' ,	'AF' ,	'HL' ,
				'IX' ,	'IY' ,	'BC' ,	'DE' ,
				'HL' ,	'AF' ,	'NZ' ,	'Z' ,
				'NC' ,	'C' ,	'PO' ,	'PE' ,
				'P' ,	'M' ,	'00h' ,	'08h' ,
				'10h' ,	'18h' ,	'20h' ,	'28h' ,
				'30h' ,	'38h'
	      ]
	}
	,'OPERATORS' :[ ]
	,'DELIMITERS' :[ ',']
	,'STYLES' : {
		 'COMMENTS'    : 'color: #99CC00;font-style:italic;'
		,'QUOTESMARKS' : 'color: #333399;'
		,'KEYWORDS' : {
			 'keywords'   : 'color: #3366FF;'
			,'statements' : 'color: #0000ff;font-weight:bold;'
		 }
		,'OPERATORS'   : 'color: #FF0000;'
		,'DELIMITERS'  : 'color: #0000FF;'
	}
};
