package com.github.braisdom.dionaea;

import com.github.braisdom.dionaea.syntax.DionaeaLexer;

import java.io.StringReader;

public class SyntaxTest {

    public static void main(String args[]) {
        DionaeaLexer lexer = new DionaeaLexer(new StringReader(""));
    }
}
