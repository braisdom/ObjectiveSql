package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.annotations.Relation;
import com.github.braisdom.funcsql.apt.APTUtils;
import com.github.braisdom.funcsql.apt.AnnotationValues;
import com.github.braisdom.funcsql.apt.JavacAnnotationHandler;
import com.github.braisdom.funcsql.relation.RelationType;
import com.sun.tools.javac.tree.JCTree;
import org.mangosdk.spi.ProviderFor;

@ProviderFor(JavacAnnotationHandler.class)
public class RelationFieldCodeGenerator extends JavacAnnotationHandler<Relation>{

    @Override
    public void handle(AnnotationValues annotationValues, JCTree ast, APTUtils aptUtils) {
        Relation relation = annotationValues.getAnnotationValue(Relation.class);

        handleRelationField(relation, aptUtils);
    }

    private void handleRelationField(Relation relation, APTUtils aptUtils) {
        RelationType relationType = relation.relationType();
    }
}
