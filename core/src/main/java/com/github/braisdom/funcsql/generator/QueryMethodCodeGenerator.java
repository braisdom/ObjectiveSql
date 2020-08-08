package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.Query;
import com.github.braisdom.funcsql.apt.JavacAnnotationHandler;
import com.github.braisdom.funcsql.apt.APTHandler;
import com.github.braisdom.funcsql.annotations.Queryable;
import com.github.braisdom.funcsql.apt.MethodBuilder;
import com.github.braisdom.funcsql.apt.StatementBuilder;
import com.github.braisdom.funcsql.util.WordUtil;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import org.mangosdk.spi.ProviderFor;

//import static com.github.braisdom.funcsql.generator.StatementBuilder.varRef;
//import static com.github.braisdom.funcsql.generator.MethodBuilder.createParameter;
//import static lombok.javac.handlers.JavacHandlerUtil.genTypeRef;
//import static lombok.javac.handlers.JavacHandlerUtil.injectMethod;

@ProviderFor(JavacAnnotationHandler.class)
public class QueryMethodCodeGenerator extends JavacAnnotationHandler<Queryable> {

    @Override
    public void handle(Queryable annotation, JCTree ast, APTHandler handler) {
        TreeMaker treeMaker = handler.getTreeMaker();
        JCTree.JCVariableDecl field = (JCTree.JCVariableDecl) handler.get();
        String fieldColumnName = WordUtil.underscore(field.getName().toString());
        String methodName = WordUtil.camelize("queryBy_" + field.getName(), true);

        MethodBuilder methodBuilder = handler.createMethodBuilder();
        StatementBuilder statementBuilder = handler.createBlockBuilder();

        methodBuilder.addParameter("value", field.vartype);

        statementBuilder.append(handler.newGenericsType(Query.class, handler.getClassName()),
                "query", null);

//        handler.inject(methodBuilder
//                .addStatements(statementBuilder.build())
//                .build(methodName, Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

//    @Override
//    public void handle(AnnotationValues<Queryable> annotation, JCTree.JCAnnotation ast, APTHandler annotationNode) {
//        APTHandler fieldNode = annotationNode.up();
//        APTHandler typeNode = fieldNode.up();
//        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
//        JCTree.JCVariableDecl field = (JCTree.JCVariableDecl) fieldNode.get();
//
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(typeNode.getTreeMaker(), typeNode);
//        String fieldColumnName = WordUtil.underscore(field.getName().toString());
//        JCTree.JCVariableDecl valueVar = createParameter(typeNode, field.vartype, "value");
//        blockBuilder.appendVar(treeMaker.TypeApply(genTypeRef(typeNode, Query.class.getName()),
//                List.of(treeMaker.Ident(typeNode.toName(typeNode.getName())))), "query",
//                treeMaker.Apply(List.nil(), treeMaker.Ident(typeNode.toName("createQuery")), List.nil()));
//        blockBuilder.inject(treeMaker.Exec(treeMaker.Apply(List.nil(), treeMaker.Select(varRef(typeNode, "query"),
//                typeNode.toName("where")), List.of(treeMaker.Literal(String.format("%s = ?",fieldColumnName)),
//                varRef(typeNode, "value")))));
//        blockBuilder.appendReturn("query", "execute");
//        injectMethod(typeNode, MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
//                .withName(WordUtil.camelize("queryBy_" + field.getName(), true))
//                .withParameters(valueVar)
//                .withThrowsClauses(SQLException.class)
//                .withReturnType(java.util.List.class, treeMaker.Ident(typeNode.toName(typeNode.getName())))
//                .withBody(blockBuilder.build())
//                .buildWith(typeNode));
//    }

}
