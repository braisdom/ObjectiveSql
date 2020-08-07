package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.apt.JavacAnnotationHandler;
import com.github.braisdom.funcsql.apt.JavacNode;
import com.github.braisdom.funcsql.annotations.Queryable;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import org.mangosdk.spi.ProviderFor;

//import static com.github.braisdom.funcsql.generator.BlockBuilder.varRef;
//import static com.github.braisdom.funcsql.generator.MethodBuilder.createParameter;
//import static lombok.javac.handlers.JavacHandlerUtil.genTypeRef;
//import static lombok.javac.handlers.JavacHandlerUtil.injectMethod;

@ProviderFor(JavacAnnotationHandler.class)
public class QueryMethodCodeGenerator extends JavacAnnotationHandler<Queryable> {

    @Override
    public void handle(Queryable annotation, JCTree ast, JavacNode javacNode) {
        TreeMaker treeMaker = javacNode.getTreeMaker();
        JCTree.JCVariableDecl field = (JCTree.JCVariableDecl) javacNode.get();

        JCTree.JCModifiers jcModifiers = treeMaker.Modifiers(Flags.PRIVATE);
        JCTree.JCVariableDecl variableDecl = treeMaker.VarDef(jcModifiers, javacNode.toName("id"),
                javacNode.typeRef(Integer.class), null);

        javacNode.append(variableDecl);
    }

//    @Override
//    public void handle(AnnotationValues<Queryable> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
//        JavacNode fieldNode = annotationNode.up();
//        JavacNode typeNode = fieldNode.up();
//        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
//        JCTree.JCVariableDecl field = (JCTree.JCVariableDecl) fieldNode.get();
//
//        BlockBuilder blockBuilder = BlockBuilder.newBlock(typeNode.getTreeMaker(), typeNode);
//        String fieldColumnName = WordUtil.underscore(field.getName().toString());
//        JCTree.JCVariableDecl valueVar = createParameter(typeNode, field.vartype, "value");
//        blockBuilder.appendVar(treeMaker.TypeApply(genTypeRef(typeNode, Query.class.getName()),
//                List.of(treeMaker.Ident(typeNode.toName(typeNode.getName())))), "query",
//                treeMaker.Apply(List.nil(), treeMaker.Ident(typeNode.toName("createQuery")), List.nil()));
//        blockBuilder.append(treeMaker.Exec(treeMaker.Apply(List.nil(), treeMaker.Select(varRef(typeNode, "query"),
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
