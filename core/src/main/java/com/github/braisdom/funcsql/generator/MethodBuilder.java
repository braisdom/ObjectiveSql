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

import static com.sun.tools.javac.util.List.nil;
import static lombok.javac.Javac.CTC_VOID;

import lombok.javac.Javac;
import lombok.javac.JavacNode;

import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.List;
import lombok.javac.JavacTreeMaker;

/**
 * Simplifies creation of methods.
 *
 * @author Alex Ruiz
 */
class MethodBuilder {

  static MethodBuilder newMethod() {
    return new MethodBuilder();
  }

  private long modifiers;
  private String name;
  private JCExpression returnType;
  private List<JCVariableDecl> parameters = nil();
  private List<JCExpression> throwsClauses = nil();
  private JCBlock body;
  private JCExpression defaultValue;

  private MethodBuilder() {}

  MethodBuilder withModifiers(long newModifiers) {
    modifiers = newModifiers;
    return this;
  }

  MethodBuilder withName(String newName) {
    name = newName;
    return this;
  }

  MethodBuilder withReturnType(JCExpression newReturnType) {
    returnType = newReturnType;
    return this;
  }

  MethodBuilder withParameters(List<JCVariableDecl> newParameters) {
    parameters = newParameters;
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
    return treeMaker.MethodDef(treeMaker.Modifiers(modifiers), node.toName(name),
            returnType, List.<JCTypeParameter>nil(), parameters, throwsClauses,
            body, null);
  }
}
