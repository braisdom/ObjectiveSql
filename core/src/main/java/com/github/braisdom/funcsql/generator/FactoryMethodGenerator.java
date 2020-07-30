package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.Database;
import com.github.braisdom.funcsql.Persistence;
import com.github.braisdom.funcsql.PersistenceFactory;
import com.github.braisdom.funcsql.annotations.DomainModel;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import lombok.core.AnnotationValues;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import org.mangosdk.spi.ProviderFor;

import static com.github.braisdom.funcsql.util.StringUtil.splitNameOf;
import static lombok.javac.handlers.JavacHandlerUtil.chainDots;
import static lombok.javac.handlers.JavacHandlerUtil.injectMethod;

@ProviderFor(JavacAnnotationHandler.class)
public class FactoryMethodGenerator extends JavacAnnotationHandler<DomainModel> {

    @Override
    public void handle(AnnotationValues<DomainModel> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
        JavacTreeMaker treeMaker = annotationNode.getTreeMaker();
        JavacNode typeNode = annotationNode.up();

        JCMethodDecl createPersistenceMethod = createPersistenceMethod(treeMaker, typeNode);

        injectMethod(typeNode, createPersistenceMethod);
    }

    private JCMethodDecl createPersistenceMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCStatement> jcStatements = new ListBuffer<>();

        jcStatements.append(
                treeMaker.VarDef(
                        treeMaker.Modifiers(Flags.PARAMETER),
                        typeNode.toName("persistenceFactory"),
                        chainDots(typeNode, splitNameOf(PersistenceFactory.class)),
                        treeMaker.Apply(
                                List.nil(),
                                treeMaker.Select(
                                        chainDots(typeNode, splitNameOf(Database.class)),
                                        typeNode.toName("getPersistenceFactory")
                                ), List.nil()
                        )
                )
        );

        jcStatements.append(
                treeMaker.Return(
                        treeMaker.Apply(
                                List.nil(),
                                treeMaker.Select(
                                        treeMaker.Ident(typeNode.toName("persistenceFactory")),
                                        typeNode.toName("persistenceFactory")
                                )
                                , List.of(
                                        treeMaker.Select(
                                                treeMaker.Ident(typeNode.toName(typeNode.getName())),
                                                typeNode.toName("class")
                                        )
                                )
                        ))
        );

        return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC + Flags.STATIC + Flags.FINAL),
                typeNode.toName("createPersistence"),
                chainDots(typeNode, splitNameOf(Persistence.class)),
                List.nil(),
                List.nil(),
                List.nil(),
                treeMaker.Block(0, jcStatements.toList()),
                null
        );
    }
}
