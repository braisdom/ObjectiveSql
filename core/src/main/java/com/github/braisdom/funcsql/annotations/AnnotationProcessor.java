package com.github.braisdom.funcsql.annotations;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
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
import java.util.ArrayList;
import java.util.Set;

@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(value = {"com.github.braisdom.funcsql.annotations.DomainModel"})
public class AnnotationProcessor extends AbstractProcessor {

    private static final String FUNC_SQL_PACKAGE = "com.github.braisdom.funcsql";

    private java.util.List<String> methodsCache = new ArrayList<>();

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

                    JCTree.JCCompilationUnit imports = (JCTree.JCCompilationUnit) trees.getPath(element).getCompilationUnit();

                    cacheMethod(jcClassDecl.defs);

                    imports.defs = imports.defs.append(
                            treeMaker.Import(
                                    treeMaker.Select(
                                            treeMaker.Ident(names.fromString("com.github.braisdom.funcsql")),
                                            names.fromString("DefaultQuery")),
                                    false)
                    );

                    imports.defs = imports.defs.append(
                            treeMaker.Import(
                                    treeMaker.Select(
                                            treeMaker.Ident(names.fromString("com.github.braisdom.funcsql")),
                                            names.fromString("Query")),
                                    false)
                    );

                    if(!methodsCache.contains("createQuery"))
                        jcClassDecl.defs = jcClassDecl.defs.append(
                                createQueryMethod(jcClassDecl, element)
                        );
                }
            });
        });

        return true;
    }

    private JCTree.JCMethodDecl createQueryMethod(JCTree.JCClassDecl jcClassDecl, Element element) {
        if(methodsCache.contains("createQuery"))
            return null;

        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        ListBuffer<JCTree.JCExpression> jcVariableExpressions = new ListBuffer<>();

        jcVariableExpressions.append(
                treeMaker.Select(
                        treeMaker.Ident(names.fromString(element.getSimpleName().toString())),
                        names.fromString("class")
                )
        );

        jcStatements.append(
                treeMaker.Return(treeMaker.NewClass(
                        null,
                        List.nil(), //泛型参数列表
                        treeMaker.Ident(names.fromString("DefaultQuery")), //创建的类名
                        jcVariableExpressions.toList(), //参数列表
                        null //类定义，估计是用于创建匿名内部类
                ))
        );

        return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC + Flags.STATIC + Flags.FINAL),
                names.fromString("createQuery"),
                treeMaker.Ident(names.fromString("Query")),
                List.nil(),
                List.nil(),
                List.nil(),
                treeMaker.Block(0, jcStatements.toList()),
                null
        );
    }

    private void cacheMethod(List<JCTree> defs) {
        for (JCTree def : defs) {
            if(def.getKind() == Tree.Kind.METHOD) {
                methodsCache.add(((JCTree.JCMethodDecl)def).getName().toString());
            }
        }
    }
}