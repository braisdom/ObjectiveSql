package com.github.braisdom.funcsql.apt;

import com.sun.tools.javac.tree.JCTree;

import java.lang.annotation.Annotation;

public abstract class JavacAnnotationHandler<T extends Annotation> {

    public abstract void handle(AnnotationValues annotationValues, JCTree ast, APTHandler handler);

    public Class<T> getAnnotationHandledByThisHandler() {
        return (Class<T>) SpiLoadUtil.findAnnotationClass(getClass(), JavacAnnotationHandler.class);
    }
}
