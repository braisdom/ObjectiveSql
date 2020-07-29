package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.annotations.DomainModel;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import lombok.core.AnnotationValues;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.handlers.JavacHandlerUtil;
import org.mangosdk.spi.ProviderFor;


@ProviderFor(JavacAnnotationHandler.class)
public class PrimaryFieldGenerator extends JavacAnnotationHandler<DomainModel> {

    @Override
    public void handle(AnnotationValues<DomainModel> annotationValues, JCTree.JCAnnotation jcAnnotation, JavacNode javacNode) {
        JavacNode typeNode = javacNode.up();
        DomainModel domainModel = annotationValues.getInstance();

        JCTree.JCVariableDecl fieldDecl = FieldBuilder.newField()
                .ofType(domainModel.primaryClass())
                .withModifiers(Flags.PRIVATE)
                .withName(domainModel.primaryFieldName())
                .buildWith(typeNode);

        JavacHandlerUtil.injectField(typeNode, fieldDecl);

        System.out.println();
    }
}
