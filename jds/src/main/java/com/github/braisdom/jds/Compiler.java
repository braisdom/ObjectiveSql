package com.github.braisdom.jds;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.util.Context;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.io.*;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
public class Compiler extends AbstractProcessor {

    private JavaCompiler javaCompiler;

    private String java = "package com.github.braisdom.funcsql.example;\n" +
            "\n" +
            "public interface HelloWorld {\n" +
            "}\n";

    private class DionaeaFileObject extends SimpleJavaFileObject {

        protected DionaeaFileObject() {
            super(URI.create("memory://dionaea"), Kind.SOURCE);
        }

        @Override
        public InputStream openInputStream() throws IOException {
            return new ByteArrayInputStream(java.getBytes());
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            return new ByteArrayOutputStream();
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return java;
        }
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        javaCompiler = JavaCompiler.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(annotations.size() > 0) {
            try {
                final String className = "HelloImmutable";
                final JavaFileObject fileObject = processingEnv.getFiler()
                        .createSourceFile("com.github.braisdom.funcsql." + className);

                try (Writer writter = fileObject.openWriter()) {
                    writter.append("package com.github.braisdom.funcsql;");
                    writter.append("\n\n");
                    writter.append("public class " + className + " {");
                    writter.append("\n");
                    writter.append("}");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedOptions = new HashSet<>();
        supportedOptions.add(Dionaea.class.getCanonicalName());
        return supportedOptions;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

}
