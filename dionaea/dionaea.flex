package com.github.braisdom.dionaea.syntax;

%%

%public
%class DionaeaLexer
%function advance
%type DionaeaType
%unicode

EOL=\R
CRLF=\R
WHITE_SPACE=[\ \n\t\f]
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

END_OF_LINE_COMMENT=("-- ")[^\r\n]*
Identifier = [a-zA-Z][a-zA-Z0-9_]*
StringCharacter = [^\r\n\"\\]
SingleCharacter = [^\r\n\'\\]
BackQuoteChars  = [^\r\n\`\\]

%state WAITING_VALUE

%%

"query"     { return DionaeaType.QUERY; }


<YYINITIAL> {
    {END_OF_LINE_COMMENT}                  { return DionaeaType.COMMENT; }
}

[^] { return DionaeaType.BAD_CHARACTER; }
