/*
 * Created on Dec 1, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package com.github.braisdom.funcsql.generator;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.List;
import lombok.javac.Javac;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

import java.util.Arrays;

import static com.sun.tools.javac.util.List.nil;
import static lombok.javac.Javac.CTC_VOID;
import static lombok.javac.handlers.JavacHandlerUtil.genTypeRef;

/**
 * Simplifies creation of methods.
 *
 * @author Alex Ruiz
 */
class MethodBuilder {

  static MethodBuilder newMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
    return new MethodBuilder(treeMaker, typeNode);
  }

  private final JavacNode typeNode;
  private final JavacTreeMaker treeMaker;
  private JCTree.JCModifiers modifiers;
  private String name;
  private JCExpression returnType;
  private List<JCVariableDecl> parameters = nil();
  private List<JCExpression> throwsClauses = nil();
  private JCBlock body;
  private JCExpression defaultValue;

  private MethodBuilder(JavacTreeMaker treeMaker, JavacNode typeNode) {
    this.treeMaker = treeMaker;
    this.typeNode = typeNode;
  }

  MethodBuilder withModifiers(long newModifiers) {
    modifiers = treeMaker.Modifiers(newModifiers);
    return this;
  }

  MethodBuilder withName(String newName) {
    name = newName;
    return this;
  }

  MethodBuilder withReturnType(String newReturnType) {
    returnType = treeMaker.Ident(typeNode.toName(newReturnType));
    return this;
  }

  MethodBuilder withReturnType(JCExpression newReturnType) {
    returnType = newReturnType;
    return this;
  }

  MethodBuilder withReturnType(JavacTreeMaker.TypeTag type) {
    returnType = treeMaker.TypeIdent(type);
    return this;
  }

  MethodBuilder withReturnType(Class typeClass) {
    returnType = genTypeRef(typeNode, typeClass.getName());
    return this;
  }

  MethodBuilder withReturnType(JCExpression newReturnType, JCExpression... genericTypes) {
    returnType = treeMaker.TypeApply(newReturnType, List.from(genericTypes));
    return this;
  }

  MethodBuilder withReturnType(Class typeClass, JCExpression... genericTypes) {
    returnType = treeMaker.TypeApply(genTypeRef(typeNode, typeClass.getName()),
            List.from(genericTypes));
    return this;
  }

  MethodBuilder withReturnType(Class typeClass, Class... genericTypeClasses) {
    JCExpression[] genericTypes = Arrays.stream(genericTypeClasses).map(genericTypeClass ->
            genTypeRef(typeNode, genericTypeClass.getName())).toArray(JCExpression[]::new);
    returnType = treeMaker.TypeApply(genTypeRef(typeNode, typeClass.getName()),
            List.from(genericTypes));
    return this;
  }

  MethodBuilder withParameters(List<JCVariableDecl> newParameters) {
    parameters = newParameters;
    return this;
  }

  MethodBuilder withParameters(JCVariableDecl... newParameters) {
    parameters = List.from(newParameters);
    return this;
  }

  MethodBuilder withThrowsClauses(Class<? extends Throwable>... exceptionClasses) {
    JCExpression[] exceptionExpressions = Arrays.stream(exceptionClasses).map(exceptionClass ->
            treeMaker.Throw(genTypeRef(typeNode, exceptionClass.getName())).getExpression())
            .toArray(JCExpression[]::new);
    throwsClauses = List.from(exceptionExpressions);
    return this;
  }

  MethodBuilder withThrowsClauses(List<JCExpression> newThrowsClauses) {
    throwsClauses = newThrowsClauses;
    return this;
  }

  MethodBuilder withBody(JCBlock newBody) {
    body = newBody;
    return this;
  }

  MethodBuilder withDefaultValue(JCExpression newDefaultValue) {
    defaultValue = newDefaultValue;
    return this;
  }

  JCMethodDecl buildWith(JavacNode node) {
    JavacTreeMaker treeMaker = node.getTreeMaker();
    if(returnType == null)
      returnType = treeMaker.Type(Javac.createVoidType(node.getTreeMaker(), CTC_VOID));
    return treeMaker.MethodDef(modifiers, node.toName(name), returnType, List.<JCTypeParameter>nil(), parameters, throwsClauses,
            body, null);
  }

  static JCVariableDecl createParameter(JavacNode typeNode, Class<?> paramType, String name) {
    return createParameter(typeNode, genTypeRef(typeNode, paramType.getName()), name);
  }

  static JCVariableDecl createParameter(JavacNode typeNode, JCExpression paramType, String name) {
    JavacTreeMaker treeMaker = typeNode.getTreeMaker();
    treeMaker.at(typeNode.get().pos);
    return treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), typeNode.toName(name),
            paramType, null);
  }
}
