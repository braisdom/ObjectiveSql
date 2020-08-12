package com.github.braisdom.funcsql.apt;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("*")
public class AnnotationProcessor extends AbstractProcessor {

    private List<JavacAnnotationHandler> handlers;
    private Messager messager;
    private Elements elementUtils;
    private JavacTrees javacTrees;
    private TreeMaker treeMaker;
    private Names names;
    private ClassLoader classloader;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        try {
            handlers = SpiLoadUtil.readAllFromIterator(SpiLoadUtil.findServices(JavacAnnotationHandler.class,
                    JavacAnnotationHandler.class.getClassLoader()));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

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
        for (JavacAnnotationHandler handler : handlers) {
            final Class<? extends Annotation> annotationClass = handler.getAnnotationHandledByThisHandler();
            final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotationClass);
            for(Element element : elements) {
                JCTree ast = javacTrees.getTree(element);
                JCTree.JCClassDecl classDecl;
                if(ast instanceof JCTree.JCClassDecl)
                    classDecl = (JCTree.JCClassDecl) ast;
                else if(ast instanceof JCTree.JCVariableDecl)
                    classDecl = getClassDecl((JCTree.JCVariableDecl) ast);
                else
                    classDecl = null;
                APTUtils aptUtils = new APTUtils(classDecl, element, ast, treeMaker, names, messager);
                handler.handle(new AnnotationValues(classDecl, classloader), ast, aptUtils);
            }
        }
        return handlers.size() > 0;
    }

    private JCTree.JCClassDecl getClassDecl(JCTree.JCVariableDecl tree) {
        String className = tree.sym.owner.getQualifiedName().toString();
        TypeElement typeElement = elementUtils.getTypeElement(className);
        return javacTrees.getTree(typeElement);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
