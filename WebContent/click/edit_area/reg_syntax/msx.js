editAreaLoader.load_syntax["msx"] = {
	'COMMENT_SINGLE' : {1 : "'", 2 : 'rem', 3 : 'REM'}
	,'COMMENT_MULTI' : { }
	,'QUOTEMARKS' : {1:'"'}
	,'KEYWORD_CASE_SENSITIVE' : false
	,'KEYWORDS' : {
		'statements' : [
			'if','then','for',
			'else','elseif',
			'next','step','to', 'call'
		]
		,'keywords' : [
			'exit', 'dim', 'as',
			'base', 'beep', 'binary', 'bload', 'bsave',
			'circle', 'clear', 'close', 'cls', 'color',
			'com', 'data', 'date','defint', 'deflng', 
			'defsng', 'defstr', 'draw', 'erase', 'error',
			'files', 'get', 'gosub', 'goto', 'key',
			'kill', 'let', 'line', 'list', 'locate',
			'lprint', 'lset', 'off', 'on', 'open',
			'out', 'output', 'paint', 'palette',
			'poke', 'preset', 'print', 'pset', 'put',
			'randomize', 'read', 'reset', 'restore',
			'return', 'rset', 'run', 'screen',
			'sound', 'stop', 'strig', 'string', 'swap', 
			'system', 'time', 'timer', 'troff', 'tron', 
			'type', 'using', 'wait', 'width', 'sprite',
			'interval', 'list', 'llist', 'lprint', 'motor',
			'new'
	        ]
		,'functions' : [
			'abs', 'asc', 'atn', 'auto','chr', 'cint',
			'cos', 'eof', 'exp', 'fix', 'fre', 'hex', 
			'inkey', 'imp', 'input', 'instr', 'int',
			'left', 'len', 'mid', 'oct', 'peek','play', 
			'point', 'pos', 'right', 'rnd', 'seek', 
			'sgn', 'sin', 'space', 'spc', 'sqr', 
			'stick', 'str', 'tab', 'tan', 'val'
		]
		,'operators' : [
			'and', 'mod', 'not', 'or', 'xor'
		]
	}
	,'OPERATORS' :[
		'+', '-', '/', '*', '=', '<', '>', '!', '&', '~'
	]
	,'DELIMITERS' :[
		'(', ')', '[', ']', ':', ';'
	]
	,'STYLES' : {
		'COMMENTS'    : 'color: #99CC00;font-style:italic;'
		,'QUOTESMARKS': 'color: #333399;'
		,'KEYWORDS' : {
			'keywords'    : 'color: #3366FF;font-weight:bold;'
			,'functions'  : 'color: #0000FF;font-weight:bold;'
			,'statements' : 'color: #3366FF;font-weight:bold;'
			,'operators'  : 'color: #FF0000;font-weight:bold;'
			}
		,'OPERATORS'  : 'color: #FF0000;'
		,'DELIMITERS' : 'color: #0000FF;'
	}
};
