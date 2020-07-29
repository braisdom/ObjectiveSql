package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import lombok.AccessLevel;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.HandleGetter;
import lombok.javac.handlers.HandleSetter;
import lombok.javac.handlers.JavacHandlerUtil;
import org.mangosdk.spi.ProviderFor;

import static com.github.braisdom.funcsql.util.StringUtil.splitNameOf;
import static lombok.javac.handlers.JavacHandlerUtil.chainDots;

@ProviderFor(JavacAnnotationHandler.class)
public class PrimaryFieldGenerator extends JavacAnnotationHandler<DomainModel> {

    @Override
    public void handle(AnnotationValues<DomainModel> annotationValues, JCTree.JCAnnotation jcAnnotation, JavacNode javacNode) {
        JavacNode typeNode = javacNode.up();
        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
        DomainModel domainModel = annotationValues.getInstance();

        JCTree.JCAnnotation annotation = treeMaker.Annotation(
                chainDots(typeNode, splitNameOf(PrimaryKey.class)),
                List.of(treeMaker.Assign(treeMaker.Ident(typeNode.toName("name")),
                        treeMaker.Literal(domainModel.primaryColumnName()))));

        JCTree.JCVariableDecl fieldDecl = FieldBuilder.newField()
                .ofType(domainModel.primaryClass())
                .withModifiers(Flags.PRIVATE)
                .withAnnotations(annotation)
                .withName(domainModel.primaryFieldName())
                .buildWith(typeNode);

        JavacNode fieldNode = new JavacNode(javacNode.getAst(), fieldDecl, null, AST.Kind.FIELD) {
            @Override
            public JavacNode up() {
                return typeNode;
            }
        };

        new HandleGetter().generateGetterForField(fieldNode,null, AccessLevel.PUBLIC, false);
        new HandleSetter().generateSetterForField(fieldNode, typeNode, AccessLevel.PUBLIC);

        JavacHandlerUtil.injectField(typeNode, fieldDecl);
    }
}
