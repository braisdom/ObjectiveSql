package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.annotations.Relation;
import com.github.braisdom.funcsql.apt.APTUtils;
import com.github.braisdom.funcsql.apt.AnnotationValues;
import com.github.braisdom.funcsql.apt.JavacAnnotationHandler;
import com.github.braisdom.funcsql.relation.RelationType;
import com.github.braisdom.funcsql.relation.Relationship;
import com.github.braisdom.funcsql.util.WordUtil;
import com.google.auto.service.AutoService;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.TreeMaker;
import org.mangosdk.spi.ProviderFor;

import javax.annotation.processing.Processor;
import java.lang.annotation.Annotation;

@AutoService(Processor.class)
public class RelationFieldCodeGenerator extends DomainModelProcessor {

    @Override
    public void handle(AnnotationValues annotationValues, JCTree ast, APTUtils aptUtils) {
        Relation relation = annotationValues.getAnnotationValue(Relation.class);
        JCTree.JCVariableDecl relationField = (JCTree.JCVariableDecl) ast;

        handleRelationField(relation, relationField, aptUtils);
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return Relation.class;
    }

    private void handleRelationField(Relation relation, JCTree.JCVariableDecl relationField, APTUtils aptUtils) {
        TreeMaker treeMaker = aptUtils.getTreeMaker();
        RelationType relationType = relation.relationType();
        String relationName = String.format("%s_%s", relationType.toString().toUpperCase(),
                WordUtil.underscore(relationField.getName().toString()).toUpperCase());
        JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL);
        JCExpression relationInit = aptUtils.staticMethodCall(Relationship.class,
                "createRelation", aptUtils.classRef(aptUtils.getClassName()), treeMaker.Literal(relationField.getName().toString()));

        aptUtils.inject(treeMaker.VarDef(modifiers, aptUtils.toName(relationName),
                aptUtils.typeRef(Relationship.class), relationInit));
        
    }
}
