package com.github.braisdom.funcsql.apt;

import com.sun.tools.javac.tree.JCTree;
import sun.misc.Launcher;

import java.lang.annotation.Annotation;

public abstract class JavacAnnotationHandler<T extends Annotation> {

    public abstract void handle(AnnotationValues annotationValues, JCTree ast, APTUtils aptUtils);

    public Class<T> getAnnotationHandledByThisHandler() {
        return (Class<T>) SpiLoadUtil.findAnnotationClass(getClass(), JavacAnnotationHandler.class);
    }

    public ClassLoader getClassLoader() {
        return ClassLoader.getSystemClassLoader();
    }
}
