import java_cup.runtime.*;

%%

%class scanner
%unicode 

%cup
%line
%column


%{
     	StringBuffer string = new StringBuffer();
%}
/*necessário para o analizador sintatico conseguir indicar onde esta o erro*/
%{
	private Symbol symbol(int type) {
		return new Symbol(type, yyline, yycolumn);
	}
	private Symbol symbol(int type, Object value) {
		return new Symbol(type, yyline, yycolumn, value);
	}
%}

%eof{ 
 System.out.println("Codigo analisado com sucesso"); 
%eof}

identifier = [:jletter:] ([:jletter:]|[:jletterdigit:])*
boolean = true | false
float = [:digit:]* "." [:digit:]+;
char = ([:jletter:] | [digit]); 
inteiro = 0 | [1-9][0-9]*

%state STRING

%%

<YYINITIAL> {
    [\n] {System.out.println("");}

    //types
    "int" "eger"? {return symbol(sym.INTEGER_KEYWORD);}
    "bool" "ean"? {return symbol(sym.BOOLEAN_KEYWORD);}
    "char" {return symbol(sym.CHARACTER_KEYWORD);}
    "float" {return symbol(sym.FLOAT);}

    //logical operators
    "=" {return symbol(sym.EQ);}
    "==" {return symbol(sym.EQEQ);}
    "&&" {return symbol(sym.AND);}
    "||" {return symbol(sym.OR);}
    "^" {return symbol(sym.XOR);}
    "!" {return symbol(sym.NOT);}

    //aritmatic operators
    "+" {return symbol(sym.ADDITION);}
    "-" {return symbol(sym.SUBTRACTION);}
    "*" {return symbol(sym.MULTIPLICATION);}
    "/" {return symbol(sym.DIVISION);}
    ">" {return symbol(sym.GREATER_THAN);}
    "<" {return symbol(sym.LESS_THAN);}

    //symbols
    "," {return symbol(sym.COMMA);}
    ";" {return symbol(sym.SEMICOLON);}
    "[" {return symbol(sym.SQUARE_BRACKET_OPEN);}
    "]" {return symbol(sym.SQUARE_BRACKET_CLOSE);}
    "(" {return symbol(sym.PARENTHESES_OPEN);}
    ")" {return symbol(sym.PARENTHESES_CLOSE);}
    "{" {return symbol(sym.CURLY_BRACKET_OPEN);}
    "}" {return symbol(sym.CURLY_BRACKET_CLOSE);}

    //keywords
    if {return symbol(sym.IF);}
    else {return symbol(sym.ELSE);}
    while {return symbol(sym.WHILE);}
    for {return symbol(sym.FOR);}
    print {return symbol(sym.PRINT);}
    scan {return symbol(sym.SCAN);}

    //literais uma parte foi tirado da documentação do jflex
    \" { string.setLength(0); yybegin(STRING); }
    {boolean} {return symbol(sym.BOOLEAN);}
    {inteiro} { return symbol(sym.INTEGER); }
    {float} { return symbol(sym.FLOAT); }
    "'"{char}"'" {return symbol(sym.CHAR);}
    {identifier} {return symbol(sym.IDENTIFIER);}
    [\t\r] { }
}

<STRING> {
    \"                             { yybegin(YYINITIAL); 
                                    return symbol(sym.STRING, 
                                    string.toString()); }
    [^\n\r\"\\]+                   { string.append( yytext() ); }
    \\t                            { string.append('\t'); }
    \\n                            { string.append('\n'); }

    \\r                            { string.append('\r'); }
    \\\"                           { string.append('\"'); }
    \\                             { string.append('\\'); }
}

. { }
[^] { throw new Error("charactere ilegal <"+yytext()+">"); }