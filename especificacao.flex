import java_cup.runtime.*;

%%

%cup
%standalone
%class lexAnalyzer
%line

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

%%

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

//identifier and variables
{boolean} {return symbol(sym.BOOLEAN);}
{identifier} {return symbol(sym.IDENTIFIER);}
[:digit:]+ {return symbol(sym.INTEGER);}

[\t\r] { }
. { }
[^] { throw new Error("charactere ilegal <"+yytext()+">"); }