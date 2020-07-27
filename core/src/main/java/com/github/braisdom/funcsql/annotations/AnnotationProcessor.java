package com.github.braisdom.funcsql.annotations;

import com.github.braisdom.funcsql.generator.FactoryMethodGenerator;
import com.github.braisdom.funcsql.generator.CodeGenerator;
import com.github.braisdom.funcsql.generator.PersistenceMethodGenerator;
import com.github.braisdom.funcsql.generator.SetterGetterMethodGenerator;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.Set;

@SupportedAnnotationTypes(value = {"com.github.braisdom.funcsql.annotations.DomainModel"})
public class AnnotationProcessor extends AbstractProcessor {

    private static final java.util.List<CodeGenerator> CODE_GENERATORS = new ArrayList<>();

    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;

    private class ComparableMethod {

        private final JCTree.JCMethodDecl methodDecl;

        public ComparableMethod(JCTree.JCMethodDecl methodDecl) {
            this.methodDecl = methodDecl;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof ComparableMethod) {
                ComparableMethod other = (ComparableMethod) obj;
                return other.methodDecl.name.equals(methodDecl.name)
                        && other.methodDecl.getParameters().size() == methodDecl.getParameters().size();
            }
            return super.equals(obj);
        }
    }

    static {
        CODE_GENERATORS.add(new FactoryMethodGenerator());
        CODE_GENERATORS.add(new SetterGetterMethodGenerator());
        CODE_GENERATORS.add(new PersistenceMethodGenerator());
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.trees = JavacTrees.instance(processingEnv);
        this.treeMaker = TreeMaker.instance(((JavacProcessingEnvironment) processingEnv).getContext());
        this.names = Names.instance(((JavacProcessingEnvironment) processingEnv).getContext());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(DomainModel.class);

        elements.forEach(element -> {
            JCTree jcTree = trees.getTree(element);
            final java.util.List<ComparableMethod> methodsCache = new ArrayList<>();
            final JCTree.JCCompilationUnit imports = (JCTree.JCCompilationUnit) trees.getPath(element).getCompilationUnit();

            jcTree.accept(new TreeTranslator() {

                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                    super.visitClassDef(jcClassDecl);
                    cacheMethod(methodsCache, jcClassDecl.defs);

                    for(CodeGenerator codeGenerator : CODE_GENERATORS) {
                        CodeGenerator.ImportItem[] importItems = codeGenerator.getImportItems();
                        JCTree.JCMethodDecl[] jcMethodDecls = codeGenerator.generateMethods(treeMaker, names, element, jcClassDecl);

                        processImport(imports, importItems);
                        processMethods(jcClassDecl, methodsCache, jcMethodDecls);
                    }
                }
            });
        });

        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void processMethods(JCTree.JCClassDecl jcClassDecl, java.util.List<ComparableMethod> methodsCache,
                                JCTree.JCMethodDecl[] jcMethodDecls) {
        for (JCTree.JCMethodDecl jcMethodDecl : jcMethodDecls) {
            if(!methodsCache.contains(new ComparableMethod(jcMethodDecl))) {
                jcClassDecl.defs = jcClassDecl.defs.append(jcMethodDecl);
                cacheMethod(methodsCache, jcMethodDecl);
            }
        }
    }

    private void processImport(JCTree.JCCompilationUnit imports, CodeGenerator.ImportItem[] importItems) {
        for(CodeGenerator.ImportItem importItem : importItems) {
            imports.defs = imports.defs.append(
                    treeMaker.Import(
                            treeMaker.Select(
                                    treeMaker.Ident(names.fromString(importItem.getPackageName())),
                                    names.fromString(importItem.getClassName())),
                            false)
            );
        }
    }

    private void cacheMethod(java.util.List<ComparableMethod> methodsCache, JCTree.JCMethodDecl methodDecl) {
        methodsCache.add(new ComparableMethod(methodDecl));
    }

    private void cacheMethod(java.util.List<ComparableMethod> methodsCache, List<JCTree> defs) {
        for (JCTree def : defs) {
            if(def.getKind() == Tree.Kind.METHOD) {
                cacheMethod(methodsCache, (JCTree.JCMethodDecl) def);
            }
        }
    }
}