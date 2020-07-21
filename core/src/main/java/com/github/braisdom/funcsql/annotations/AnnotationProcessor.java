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
                    cacheMethod(jcClassDecl.defs);

                    processImport(element);
                    processQueryMethod(jcClassDecl, element);
                    processUpdateMethod(jcClassDecl, element);
                    processDeleteMethod(jcClassDecl, element);
                }
            });
        });

        return true;
    }

    private void processImport(Element element) {
        JCTree.JCCompilationUnit imports = (JCTree.JCCompilationUnit) trees.getPath(element).getCompilationUnit();
        imports.defs = imports.defs.append(
                treeMaker.Import(
                        treeMaker.Select(
                                treeMaker.Ident(names.fromString("com.github.braisdom.funcsql")),
                                names.fromString("DefaultQuery")),
                        false)
        ).append(
                treeMaker.Import(
                        treeMaker.Select(
                                treeMaker.Ident(names.fromString("com.github.braisdom.funcsql")),
                                names.fromString("DefaultUpdate")),
                        false)
        ).append(
                treeMaker.Import(
                        treeMaker.Select(
                                treeMaker.Ident(names.fromString("com.github.braisdom.funcsql")),
                                names.fromString("DefaultDelete")),
                        false)
        );

        imports.defs = imports.defs.append(
                treeMaker.Import(
                        treeMaker.Select(
                                treeMaker.Ident(names.fromString("com.github.braisdom.funcsql")),
                                names.fromString("Query")),
                        false)
        ).append(
                treeMaker.Import(
                        treeMaker.Select(
                                treeMaker.Ident(names.fromString("com.github.braisdom.funcsql")),
                                names.fromString("Update")),
                        false)
        ).append(
                treeMaker.Import(
                        treeMaker.Select(
                                treeMaker.Ident(names.fromString("com.github.braisdom.funcsql")),
                                names.fromString("Delete")),
                        false)
        );
    }

    private void processQueryMethod(JCTree.JCClassDecl jcClassDecl, Element element) {
        if(methodsCache.contains("createQuery"))
            return;

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

        JCTree.JCMethodDecl methodDecl = treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC + Flags.STATIC + Flags.FINAL),
                names.fromString("createQuery"),
                treeMaker.Ident(names.fromString("Query")),
                List.nil(),
                List.nil(),
                List.nil(),
                treeMaker.Block(0, jcStatements.toList()),
                null
        );

        jcClassDecl.defs = jcClassDecl.defs.append(methodDecl);
    }

    private void processUpdateMethod(JCTree.JCClassDecl jcClassDecl, Element element) {
        if(methodsCache.contains("createUpdate"))
            return;

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
                        treeMaker.Ident(names.fromString("DefaultUpdate")), //创建的类名
                        jcVariableExpressions.toList(), //参数列表
                        null //类定义，估计是用于创建匿名内部类
                ))
        );

        JCTree.JCMethodDecl methodDecl = treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC + Flags.STATIC + Flags.FINAL),
                names.fromString("createUpdate"),
                treeMaker.Ident(names.fromString("Update")),
                List.nil(),
                List.nil(),
                List.nil(),
                treeMaker.Block(0, jcStatements.toList()),
                null
        );

        jcClassDecl.defs = jcClassDecl.defs.append(methodDecl);
    }

    private void processDeleteMethod(JCTree.JCClassDecl jcClassDecl, Element element) {
        if(methodsCache.contains("createDelete"))
            return;

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
                        treeMaker.Ident(names.fromString("DefaultDelete")), //创建的类名
                        jcVariableExpressions.toList(), //参数列表
                        null //类定义，估计是用于创建匿名内部类
                ))
        );

        JCTree.JCMethodDecl methodDecl = treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC + Flags.STATIC + Flags.FINAL),
                names.fromString("createDelete"),
                treeMaker.Ident(names.fromString("Delete")),
                List.nil(),
                List.nil(),
                List.nil(),
                treeMaker.Block(0, jcStatements.toList()),
                null
        );

        jcClassDecl.defs = jcClassDecl.defs.append(methodDecl);
    }

    private void cacheMethod(List<JCTree> defs) {
        for (JCTree def : defs) {
            if(def.getKind() == Tree.Kind.METHOD) {
                methodsCache.add(((JCTree.JCMethodDecl)def).getName().toString());
            }
        }
    }
}