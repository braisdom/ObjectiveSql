/*
 * Created on Nov 30, 2010
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

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

import static com.github.braisdom.funcsql.util.StringUtil.splitNameOf;
import static com.sun.tools.javac.util.List.nil;
import static lombok.javac.handlers.JavacHandlerUtil.chainDots;

/**
 * Simplifies creation of fields.
 *
 * @author Alex Ruiz
 */
class FieldBuilder {

  private JavacNode node;

  static FieldBuilder newField(JavacNode node) {
    return new FieldBuilder(node);
  }

  private JCTree.JCAnnotation[] annotations = {};
  private JCExpression type;
  private String name;
  private long modifiers;
  private List<JCExpression> args = nil();

  private FieldBuilder(JavacNode node) {
    this.node = node;
  }

  FieldBuilder ofType(Class<?> newType) {
    type = chainDots(node, splitNameOf(newType));
    return this;
  }

  FieldBuilder ofType(JCExpression newType) {
    type = newType;
    return this;
  }

  FieldBuilder withName(String newName) {
    name = newName;
    return this;
  }

  FieldBuilder withModifiers(long newModifiers) {
    modifiers = newModifiers;
    return this;
  }

  FieldBuilder withAnnotations(JCTree.JCAnnotation... annotations) {
    this.annotations = annotations;
    return this;
  }

  FieldBuilder withArgs(JCExpression... newArgs) {
    args = List.from(newArgs);
    return this;
  }

  JCVariableDecl build() {
    JavacTreeMaker treeMaker = node.getTreeMaker();
    JCTree.JCModifiers jcModifiers = treeMaker.Modifiers(modifiers);

    for(JCTree.JCAnnotation annotation : annotations) {
      jcModifiers.annotations = jcModifiers.annotations.append(annotation);
    }
    treeMaker.at(node.get().pos);
    return treeMaker.VarDef(jcModifiers, node.toName(name), type, null);
  }

  private FieldBuilder() {}
}
