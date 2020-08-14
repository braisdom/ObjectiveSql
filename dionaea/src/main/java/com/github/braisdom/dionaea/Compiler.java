package com.github.braisdom.dionaea;

import com.github.braisdom.funcsql.apt.APTUtils;
import com.github.braisdom.funcsql.apt.AnnotationValues;
import com.github.braisdom.funcsql.apt.JavacAnnotationHandler;
import com.sun.tools.javac.tree.JCTree;
import org.mangosdk.spi.ProviderFor;

@ProviderFor(JavacAnnotationHandler.class)
public class Compiler extends JavacAnnotationHandler<Dionaea> {

    @Override
    public void handle(AnnotationValues annotationValues, JCTree ast, APTUtils aptUtils) {
        System.out.println();
    }
}
