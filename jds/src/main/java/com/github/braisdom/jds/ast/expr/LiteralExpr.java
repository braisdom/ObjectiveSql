/*
 * Copyright (C) 2007-2010 Júlio Vilmar Gesser.
 * Copyright (C) 2011, 2013-2020 The JavaParser Team.
 *
 * This file is part of JavaParser.
 *
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * b) the terms of the Apache License
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */
package com.github.braisdom.jds.ast.expr;

import com.github.braisdom.jds.ast.AllFieldsConstructor;
import com.github.braisdom.jds.ast.Node;
import com.github.braisdom.jds.ast.visitor.CloneVisitor;
import com.github.braisdom.jds.metamodel.JavaParserMetaModel;
import com.github.braisdom.jds.metamodel.LiteralExprMetaModel;
import com.github.braisdom.jds.TokenRange;
import com.github.braisdom.jds.ast.Generated;
import java.util.function.Consumer;
import java.util.Optional;

/**
 * A base class for all literal expressions.
 *
 * @author Julio Vilmar Gesser
 */
public abstract class LiteralExpr extends Expression {

    @AllFieldsConstructor
    public LiteralExpr() {
        this(null);
    }

    /**
     * This constructor is used by the parser and is considered private.
     */
    @Generated("com.github.braisdom.jds.generator.core.node.MainConstructorGenerator")
    public LiteralExpr(TokenRange tokenRange) {
        super(tokenRange);
        customInitialization();
    }

    @Override
    @Generated("com.github.braisdom.jds.generator.core.node.RemoveMethodGenerator")
    public boolean remove(Node node) {
        if (node == null)
            return false;
        return super.remove(node);
    }

    @Override
    @Generated("com.github.braisdom.jds.generator.core.node.CloneGenerator")
    public LiteralExpr clone() {
        return (LiteralExpr) accept(new CloneVisitor(), null);
    }

    @Override
    @Generated("com.github.braisdom.jds.generator.core.node.GetMetaModelGenerator")
    public LiteralExprMetaModel getMetaModel() {
        return JavaParserMetaModel.literalExprMetaModel;
    }

    @Override
    @Generated("com.github.braisdom.jds.generator.core.node.ReplaceMethodGenerator")
    public boolean replace(Node node, Node replacementNode) {
        if (node == null)
            return false;
        return super.replace(node, replacementNode);
    }

    @Override
    @Generated("com.github.braisdom.jds.generator.core.node.TypeCastingGenerator")
    public boolean isLiteralExpr() {
        return true;
    }

    @Override
    @Generated("com.github.braisdom.jds.generator.core.node.TypeCastingGenerator")
    public LiteralExpr asLiteralExpr() {
        return this;
    }

    @Generated("com.github.braisdom.jds.generator.core.node.TypeCastingGenerator")
    public void ifLiteralExpr(Consumer<LiteralExpr> action) {
        action.accept(this);
    }

    @Override
    @Generated("com.github.braisdom.jds.generator.core.node.TypeCastingGenerator")
    public Optional<LiteralExpr> toLiteralExpr() {
        return Optional.of(this);
    }
}
