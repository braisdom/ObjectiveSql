package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.annotations.DomainModel;
import com.sun.tools.javac.tree.JCTree;
import lombok.core.AnnotationValues;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import org.mangosdk.spi.ProviderFor;

@ProviderFor(JavacAnnotationHandler.class)
public class FactoryMethodGenerator extends JavacAnnotationHandler<DomainModel> {

    @Override
    public void handle(AnnotationValues<DomainModel> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {

    }

}
