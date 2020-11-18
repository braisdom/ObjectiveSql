/*
 * Copyright (C) 2009-2013 The Project Lombok Authors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.braisdom.objsql.apt;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCAssign;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationValues {

    private final Map<String, AnnotationValue> values;
    private final ClassLoader classLoader;

    private class AnnotationValue {

        private Map<String, Object> annotationValueMap;

        public AnnotationValue(JCAnnotation annotation) {
            annotationValueMap = new HashMap<>();

            extractAnnotationValue(annotation);
        }

        private void extractAnnotationValue(JCAnnotation annotation) {
            for (JCTree.JCExpression expression : annotation.getArguments()) {
                if (expression instanceof JCAssign) {
                    JCAssign assign = (JCAssign) expression;
                    String attributeName = ((JCTree.JCIdent) assign.lhs).name.toString();
                    if (assign.rhs instanceof JCTree.JCFieldAccess) {
                        JCTree.JCFieldAccess fieldAccess = (JCTree.JCFieldAccess) assign.rhs;
                        // TODO A ClassNotFoundException will be cached when the annotation value is not a System Class
                        if ("java.lang.Class".equalsIgnoreCase(expression.type.tsym.toString())) {
                            // For Class value
                            String className = ((Type.ClassType) fieldAccess.type).allparams_field.get(0).toString();
                            try {
                                annotationValueMap.put(attributeName,
                                        Class.forName(className, true, classLoader));
                            } catch (ClassNotFoundException ex) {
                                annotationValueMap.put(attributeName, className);
                            }
                        } else {
                            // For Enum value
                            String className = assign.rhs.type.toString();
                            try {
                                Class enumClass = Class.forName(className);
                                annotationValueMap.put(attributeName, getEnumValue(enumClass,
                                        ((JCTree.JCFieldAccess) assign.rhs).name.toString()));
                            } catch (ClassNotFoundException ex) {
                                annotationValueMap.put(attributeName, className);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else if (assign.rhs instanceof JCTree.JCLiteral) {
                        if ("boolean".equalsIgnoreCase(assign.rhs.type.toString())) {
                            annotationValueMap.put(attributeName, Boolean.valueOf(assign.rhs.toString()));
                        } else {
                            annotationValueMap.put(attributeName,
                                    ((JCTree.JCLiteral) assign.rhs).value);
                        }
                    }
                }
            }
        }

        private Object getEnumValue(Class enumClass, String value) throws NoSuchMethodException,
                InvocationTargetException, IllegalAccessException {
            Method method = enumClass.getDeclaredMethod("valueOf", String.class);
            return method.invoke(null, value);
        }

        private Object getValue(Method method) {
            return annotationValueMap.get(method.getName());
        }
    }

    public AnnotationValues(Tree tree, ClassLoader classLoader) {
        this.values = new HashMap<>();
        this.classLoader = classLoader;
        extractAnnotation(tree);
    }

    private void extractAnnotation(Tree tree) {
        List<JCAnnotation> annotations = Collections.emptyList();
        if (tree instanceof ClassTree) {
            annotations = (List<JCAnnotation>) ((ClassTree) tree).getModifiers().getAnnotations();
        } else if (tree instanceof VariableTree) {
            annotations = (List<JCAnnotation>) ((VariableTree) tree).getModifiers().getAnnotations();
        } else if (tree instanceof JCTree.JCMethodDecl) {
            annotations = ((JCTree.JCMethodDecl) tree).getModifiers().getAnnotations();
        }

        for (JCAnnotation annotation : annotations) {
            values.put(annotation.getAnnotationType().type.toString(), new AnnotationValue(annotation));
        }
    }

    public <A> A getAnnotationValue(Class<A> annotationClass) {
        return (A) Proxy.newProxyInstance(annotationClass.getClassLoader(), new Class[]{annotationClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                AnnotationValue annotationValue = values.get(method.getDeclaringClass().getName());
                if (annotationValue == null) {
                    return method.getDefaultValue();
                }

                Object value = annotationValue.getValue(method);
                if (value == null) {
                    return method.getDefaultValue();
                }

                return value;
            }
        });
    }

}
