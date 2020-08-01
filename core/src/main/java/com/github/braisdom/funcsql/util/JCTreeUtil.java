package com.github.braisdom.funcsql.util;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;

import java.util.List;

public class JCTreeUtil {

    public static boolean containsMethod(Symbol.ClassSymbol classSymbol, JCTree.JCMethodDecl methodDecl) {
        List<Symbol> methodSymbols = classSymbol.getEnclosedElements();
        String methodName = methodDecl.name.toString();
        int paramCount = methodDecl.params.size();

        for(Symbol symbol : methodSymbols) {
            if(symbol instanceof Symbol.MethodSymbol) {
                Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol) symbol;
                if(methodName.equals(methodSymbol.name.toString()) && paramCount == methodDecl.params.size())
                    return true;
            }
        }
        Symbol.TypeSymbol superclass = classSymbol.getSuperclass().tsym;
        if("java.lang.Object".equals(superclass.toString()))
            return false;
        else return containsMethod((Symbol.ClassSymbol) superclass, methodDecl);
    }
}
