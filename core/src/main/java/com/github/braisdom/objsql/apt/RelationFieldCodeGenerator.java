package com.github.braisdom.objsql.apt;

import com.github.braisdom.objsql.annotations.Relation;
import com.github.braisdom.objsql.relation.RelationType;
import com.github.braisdom.objsql.relation.Relationship;
import com.github.braisdom.objsql.util.WordUtil;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.TreeMaker;
import org.mangosdk.spi.ProviderFor;

import javax.annotation.processing.Processor;
import java.lang.annotation.Annotation;

@ProviderFor(Processor.class)
public class RelationFieldCodeGenerator extends DomainModelProcessor {

    @Override
    public void handle(AnnotationValues annotationValues, JCTree ast, APTBuilder aptBuilder) {
        Relation relation = annotationValues.getAnnotationValue(Relation.class);
        JCTree.JCVariableDecl relationField = (JCTree.JCVariableDecl) ast;

        if(ast == null || relationField == null) {
            return;
        }

        handleRelationField(relation, relationField, aptBuilder);
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return Relation.class;
    }

    private void handleRelationField(Relation relation, JCTree.JCVariableDecl relationField, APTBuilder aptBuilder) {
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        RelationType relationType = relation.relationType();
        String relationName = String.format("%s_%s", relationType.toString().toUpperCase(),
                WordUtil.underscore(relationField.getName().toString()).toUpperCase());
        JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL);
        JCExpression relationInit = aptBuilder.staticMethodCall(Relationship.class,
                "createRelation", aptBuilder.classRef(aptBuilder.getClassName()), treeMaker.Literal(relationField.getName().toString()));

        aptBuilder.inject(treeMaker.VarDef(modifiers, aptBuilder.toName(relationName),
                aptBuilder.typeRef(Relationship.class), relationInit));
        
    }
}
