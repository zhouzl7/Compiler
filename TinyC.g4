grammar TinyC;

expression:
	Identifier								# expressionToIdentifier
	| Constant								# expressionToConstant
	| String								# expressionToString
	| LParenthesis expression RParenthesis	# expressionToParenthesis
	| expression (
		(LBracket expression RBracket)
		| (LParenthesis argumentExpressionList? RParenthesis)
		| (Dot Identifier)
	)															# expressionToIndex
	| expression Type = (SelfIncrement | SelfDecrement)			# expressionToSelf
	| Type = (Add | Sub | Not | BitNot) expression				# expressionToUnary
	| expression Type = (Mul | Div | Mod) expression			# expressionToMul_Div_Mod
	| expression Type = (Add | Sub) expression					# expressionToAdd_Sub
	| expression Type = (Less | Greater | LEq | GEq) expression	# expressionToLess_Greater_LEq_GEq
	| expression Type = (Eq | NEq) expression					# expressionToEq_NEq
	| expression BitAnd expression								# expressionToBitAnd
	| expression BitXor expression								# expressionToBitXor
	| expression BitOr expression								# expressionToBitOr
	| expression And expression									# expressionToAnd
	| expression Or expression									# expressionToOr
	| expression Question expression Colon expression			# expressionToSelect
	| expression Type = (
		Assign
		| MulAssign
		| DivAssign
		| ModAssign
		| AddAssign
		| SubAssign
	) expression					# expressionToAssign
	| expression Comma expression	# expressionToMoreExpression;

argumentExpressionList:
	expression									# argumentExpressionListToExpression
	| argumentExpressionList Comma expression	# argumentExpressionListToArgumentExpressionList;

declaration:
	atomVariableType initDeclaratorList Semicolon		# declarationToDeclarationSpecifierWithInit
	| Struct Identifier Identifier (Comma Identifier)*	# declarationToStruct
	| structVariableType LBrace (
		variableType structDeclarator (Comma structDeclarator)* Semicolon
	)+ RBrace Semicolon # declarationToStructDeclarationSpecifier;

initDeclaratorList:
	initDeclarator								# initDeclaratorListToInitDeclarator
	| initDeclaratorList Comma initDeclarator	# initDeclaratorListToInitDeclaratorList;

initDeclarator:
	Identifier									# initDeclaratorToDeclarator
	| Identifier LBracket expression? RBracket	# initDeclaratorToArrayDeclarator
	| Identifier Assign expression				# initDeclaratorToDeclaratorWithAssign;

structDeclarator:
	Identifier									# structDeclaratorToIdentifier
	| Identifier LBracket expression? RBracket	# structDeclaratorToBracketExpression;

statement:
	labeledStatement		# statementToLabeledStatement
	| compoundStatement		# statementToCompoundStatement
	| expressionStatement	# statementToExpressionStatement
	| selectionStatement	# statementToSelectionStatement
	| iterationStatement	# statementToIterationStatement
	| jumpStatement			# statementToJumpStatement;

labeledStatement:
	Identifier Colon statement			# labeledStatementToIdentifier
	| Case expression Colon statement	# labeledStatementToCase
	| Default Colon statement			# labeledStatementToDefault;

compoundStatement: LBrace blockItem* RBrace;

blockItem:
	statement		# blockItemToStatement
	| declaration	# blockItemToDeclaration;

expressionStatement: expression? Semicolon;

selectionStatement:
	If LParenthesis expression RParenthesis statement (
		Else statement
	)?														# selectionStatementToIf
	| Switch LParenthesis expression RParenthesis statement	# selectionStatementToSwitch;

iterationStatement:
	While LParenthesis expression RParenthesis statement				# iterationStatementToWhile
	| Do statement While LParenthesis expression RParenthesis Semicolon	# iterationStatementToDo
	| For LParenthesis forCondition RParenthesis statement				# iterationStatementToFor;

forCondition:
	forDeclaration Semicolon forExpression? Semicolon forExpression?	# forConditionToForDeclaration
	| expression? Semicolon forExpression? Semicolon forExpression?		# forConditionToExpression;

forDeclaration:
	atomVariableType initDeclaratorList # forDeclarationToInitDeclarationSpecifier;

forExpression:
	expression							# forExpressionToExpression
	| forExpression Comma expression	# forExpressionToForExpression;

jumpStatement:
	Continue Semicolon				# jumpStatementToContinue
	| Break Semicolon				# jumpStatementToBreak
	| Return expression? Semicolon	# jumpStatementToReturn;

compilationUnit: externalDeclaration* EOF;

externalDeclaration:
	functionDefinition	# externalDeclarationToFunctionDefinition
	| declaration		# externalDeclarationToDeclaration
	| Semicolon			# externalDeclarationToSemicolon;

functionDefinition:
	variableType functionDeclarator compoundStatement;

functionDeclarator:
	Identifier LParenthesis parameterTypeList? RParenthesis # functionDeclaratorToParameterList;

parameterTypeList:
	parameterDeclaration (Comma parameterDeclaration)*;

parameterDeclaration: variableType parameterDeclarator;

parameterDeclarator:
	Identifier
	| Identifier LBracket expression? RBracket;

variableType: Void | Char | Int | Double | Struct Identifier;

atomVariableType: Void | Char | Int | Double;

structVariableType: Struct Identifier;

Break: 'break';
Case: 'case';
Char: 'char';
Continue: 'continue';
Default: 'default';
Do: 'do';
Double: 'double';
Else: 'else';
For: 'for';
If: 'if';
Int: 'int';
Return: 'return';
Struct: 'struct';
Switch: 'switch';
Void: 'void';
While: 'while';

LParenthesis: '(';
RParenthesis: ')';
LBracket: '[';
RBracket: ']';
LBrace: '{';
RBrace: '}';

Less: '<';
LEq: '<=';
Greater: '>';
GEq: '>=';

Add: '+';
Sub: '-';
Mul: '*';
Div: '/';
Mod: '%';
SelfIncrement: '++';
SelfDecrement: '--';

BitAnd: '&';
BitOr: '|';
BitNot: '~';
BitXor: '^';

And: '&&';
Or: '||';
Not: '!';

Question: '?';
Colon: ':';
Semicolon: ';';
Comma: ',';

Assign: '=';
MulAssign: '*=';
DivAssign: '/=';
ModAssign: '%=';
AddAssign: '+=';
SubAssign: '-=';

Eq: '==';
NEq: '!=';
Dot: '.';
Ellipsis: '...';

Identifier: Nondigit (Nondigit | Digit)*;

fragment Nondigit: [a-zA-Z_];

fragment Digit: [0-9];

Constant:
	IntegerConstant
	| FloatingConstant
	| CharacterConstant;

fragment IntegerConstant: DecimalConstant | HexadecimalConstant;

fragment DecimalConstant: NonzeroDigit Digit* | ZeroDigit;

fragment HexadecimalConstant:
	HexadecimalPrefix HexadecimalDigit+;

fragment HexadecimalPrefix: ZeroDigit [xX];

fragment NonzeroDigit: [1-9];

fragment ZeroDigit: '0';

fragment HexadecimalDigit: [0-9a-fA-F];

fragment FloatingConstant:
	DigitSequence? '.' DigitSequence
	| DigitSequence '.';

DigitSequence: Digit+;

fragment CharacterConstant: '\'' CCharSequence '\'';

fragment CCharSequence: CChar+;

fragment CChar: ~['\\\r\n] | EscapeSequence;
fragment EscapeSequence:
	SimpleEscapeSequence
	| HexadecimalEscapeSequence;

fragment SimpleEscapeSequence: '\\' ['"?abfnrtv\\];

fragment HexadecimalEscapeSequence: '\\x' HexadecimalDigit+;

String: '"' CCharSequence? '"';

Whitespace: [ \t]+ -> skip;

Newline: ('\r' '\n'? | '\n') -> skip;

BlockComment: '/*' .*? '*/' -> skip;

LineComment: '//' ~[\r\n]* -> skip;

Include: '#' ~[\r\n]* -> skip;