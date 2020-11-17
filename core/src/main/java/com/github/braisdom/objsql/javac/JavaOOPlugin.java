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
package com.github.braisdom.objsql.javac;

import com.github.braisdom.objsql.apt.APTBuilder;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.*;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;
import org.mangosdk.spi.ProviderFor;

@ProviderFor(Plugin.class)
public class JavaOOPlugin implements Plugin {

    public static final String NAME = "JavaOO";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void init(JavacTask task, String... args) {
        BasicJavacTask javacTask = (BasicJavacTask) task;
        Context context = javacTask.getContext();
        TreeMaker treeMaker = TreeMaker.instance(context);
        Names names = Names.instance(context);

        APTBuilder aptBuilder = new APTBuilder(treeMaker, names);

        task.addTaskListener(new TaskListener() {
            @Override
            public void started(TaskEvent e) {
            }

            @Override
            public void finished(TaskEvent e) {
                if (e.getKind() != TaskEvent.Kind.PARSE) {
                    return;
                }
                e.getCompilationUnit()
                        .accept(new TreeScanner<Void, Void>() {
                            @Override
                            public Void visitVariable(VariableTree node, Void unused) {
                                JCVariableDecl variableDecl = (JCVariableDecl) node;
                                if (variableDecl.init != null && variableDecl.init instanceof JCBinary) {
                                    JCBinary jcBinary = (JCBinary)variableDecl.init;
                                    if(!isEqOrNe(jcBinary.getTag())) {
                                        JCExpression expression = JCBinarys.createOperatorExpr(aptBuilder,
                                                jcBinary);
                                        variableDecl.init = expression;
                                    }
                                }

                                return super.visitVariable(node, unused);
                            }

                            @Override
                            public Void visitReturn(ReturnTree node, Void unused) {
                                JCReturn jcReturn = (JCReturn) node;
                                if(jcReturn.expr instanceof JCBinary) {
                                    JCBinary jcBinary = (JCBinary)jcReturn.expr;
                                    if(!isEqOrNe(jcBinary.getTag())) {
                                        JCExpression expression = JCBinarys.createOperatorExpr(aptBuilder,
                                                jcBinary);
                                        jcReturn.expr = expression;
                                    }
                                }
                                return super.visitReturn(node, unused);
                            }

                            @Override
                            public Void visitMethodInvocation(MethodInvocationTree node, Void unused) {
                                ListBuffer<JCExpression> newArgs = new ListBuffer<>();
                                List<JCExpression> args = (List<JCExpression>) node.getArguments();
                                for (ExpressionTree arg : args) {
                                    if (arg instanceof JCBinary) {
                                        JCBinary jcBinary = (JCBinary)arg;
                                        if(!isEqOrNe(jcBinary.getTag())) {
                                            JCExpression expression = JCBinarys.createOperatorExpr(aptBuilder,
                                                    jcBinary);
                                            newArgs = newArgs.append(expression);
                                        }else {
                                            newArgs = newArgs.append((JCExpression) arg);
                                        }
                                    } else {
                                        newArgs = newArgs.append((JCExpression) arg);
                                    }
                                }

                                JCMethodInvocation methodInvocation = (JCMethodInvocation) node;
                                methodInvocation.args = newArgs.toList();

                                return super.visitMethodInvocation(node, unused);
                            }
                        }, null);
            }
        });
    }

    public boolean isEqOrNe(Tag tag) {
        return tag.equals(Tag.EQ) || tag.equals(Tag.NE);
    }
}
