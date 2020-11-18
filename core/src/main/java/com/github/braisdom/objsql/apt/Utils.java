package com.github.braisdom.objsql.apt;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;

import java.util.List;
import java.util.StringTokenizer;

final class Utils {

    public static String camelize(String word, boolean firstLetterInLowerCase) {
        if (word == null || "".equals(word)) {
            return word;
        }

        String result = "";
        if (word.indexOf('_') != -1) {
            StringBuilder sb = new StringBuilder();
            int count = 0;
            StringTokenizer st = new StringTokenizer(word, "_");
            while(st.hasMoreTokens()) {
                String token = st.nextToken();
                count++;
                if (count == 1) {
                    sb.append(camelizeOneWord(token, firstLetterInLowerCase));
                }
                else {
                    sb.append(camelizeOneWord(token, false));
                }
            }
            result = sb.toString();
        }
        else {
            result = camelizeOneWord(word, firstLetterInLowerCase);
        }
        return result;
    }

    public static boolean containsMethod(Symbol.ClassSymbol classSymbol, JCTree.JCMethodDecl methodDecl, boolean recursive) {
        List<Symbol> methodSymbols = classSymbol.getEnclosedElements();
        String methodName = methodDecl.name.toString();
        int paramCount = methodDecl.params.size();

        for (Symbol symbol : methodSymbols) {
            if (symbol instanceof Symbol.MethodSymbol) {
                Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol) symbol;
                if (methodName.equals(methodSymbol.name.toString()) && paramCount == methodDecl.params.size()) {
                    return true;
                }
            }
        }
        if (recursive) {
            Symbol.TypeSymbol superclass = classSymbol.getSuperclass().tsym;
            if ("java.lang.Object".equals(superclass.toString())) {
                return false;
            } else {
                return containsMethod((Symbol.ClassSymbol) superclass, methodDecl, recursive);
            }
        } else {
            return false;
        }
    }

    private static String camelizeOneWord(String word, boolean firstLetterInLowerCase) {
        if (word == null || "".equals(word)) {
            return word;
        }

        String firstChar = word.substring(0,1);
        String result = (firstLetterInLowerCase)?firstChar.toLowerCase():firstChar.toUpperCase();
        if (word.length() > 1) {
            result += word.substring(1);
        }
        return result;
    }
}
