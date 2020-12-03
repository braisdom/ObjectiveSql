package com.github.braisdom.objsql.apt;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public abstract class DomainModelProcessor extends AbstractProcessor {
    private Messager messager;
    private Elements elementUtils;
    private JavacTrees javacTrees;
    private TreeMaker treeMaker;
    private Names names;
    private ClassLoader classloader;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.classloader = ClassLoader.getSystemClassLoader();
        this.messager = processingEnv.getMessager();
        this.elementUtils = processingEnv.getElementUtils();
        this.javacTrees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final Class<? extends Annotation> annotationClass = getAnnotationClass();
        final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotationClass);
        for(Element element : elements) {
            JCTree ast = javacTrees.getTree(element);
            JCTree.JCClassDecl classDecl;
            if(ast instanceof JCTree.JCClassDecl) {
                classDecl = (JCTree.JCClassDecl) ast;
            } else if(ast instanceof JCTree.JCVariableDecl) {
                classDecl = getClassDecl((JCTree.JCVariableDecl) ast);
            } else if(ast instanceof JCTree.JCMethodDecl) {
                classDecl = getClassDecl((JCTree.JCMethodDecl) ast);
            } else {
                classDecl = null;
            }
            APTBuilder aptBuilder = new APTBuilder(classDecl, element, ast, treeMaker, names, messager);
            handle(new AnnotationValues(ast, classloader), ast, aptBuilder);
        }
        return true;
    }

    private JCTree.JCClassDecl getClassDecl(JCTree.JCVariableDecl tree) {
        String className = tree.sym.owner.getQualifiedName().toString();
        TypeElement typeElement = elementUtils.getTypeElement(className);
        return javacTrees.getTree(typeElement);
    }

    private JCTree.JCClassDecl getClassDecl(JCTree.JCMethodDecl tree) {
        String className = tree.sym.owner.getQualifiedName().toString();
        TypeElement typeElement = elementUtils.getTypeElement(className);
        return javacTrees.getTree(typeElement);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedOptions = new HashSet<>();
        supportedOptions.add(getAnnotationClass().getCanonicalName());
        return supportedOptions;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    protected abstract Class<? extends Annotation> getAnnotationClass();

    protected abstract void handle(AnnotationValues annotationValues, JCTree ast, APTBuilder aptBuilder);
}
