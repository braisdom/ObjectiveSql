package com.github.braisdom.funcsql.annotations;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(value = {"com.github.braisdom.funcsql.annotations.DomainModel"})
public class AnnotationProcessor extends AbstractProcessor {

    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.trees = JavacTrees.instance(processingEnv);
        this.treeMaker = TreeMaker.instance(((JavacProcessingEnvironment) processingEnv).getContext());
        this.names = Names.instance(((JavacProcessingEnvironment) processingEnv).getContext());

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(DomainModel.class);

        elements.forEach(element -> {
            JCTree jcTree = trees.getTree(element);
            jcTree.accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                    super.visitClassDef(jcClassDecl);
                    jcClassDecl.defs = jcClassDecl.defs.append(
                            createFindByIdMethod()
                    );
                }
            });
        });

        return true;
    }

    private JCTree.JCMethodDecl createFindByIdMethod() {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();


        JCTree.JCBlock jcBlock = treeMaker.Block(0, jcStatements.toList());

        return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC),
                names.fromString("findById"),
                treeMaker.TypeIdent(TypeTag.VOID),
                List.nil(),
                List.nil(),
                List.nil(),
                jcBlock,
                null
        );
    }
}