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
import com.github.braisdom.jds.ast.visitor.GenericVisitor;
import com.github.braisdom.jds.ast.visitor.VoidVisitor;
import com.github.braisdom.jds.ast.Node;
import com.github.braisdom.jds.ast.visitor.CloneVisitor;
import com.github.braisdom.jds.metamodel.MarkerAnnotationExprMetaModel;
import com.github.braisdom.jds.metamodel.JavaParserMetaModel;
import com.github.braisdom.jds.TokenRange;
import java.util.function.Consumer;
import java.util.Optional;
import com.github.braisdom.jds.ast.Generated;
import static com.github.braisdom.jds.StaticJavaParser.parseName;

/**
 * An annotation that uses only the annotation type name.
 * <br>{@code @Override}
 *
 * @author Julio Vilmar Gesser
 */
public class MarkerAnnotationExpr extends AnnotationExpr {

    public MarkerAnnotationExpr() {
        this(null, new Name());
    }

    public MarkerAnnotationExpr(final String name) {
        this(null, parseName(name));
    }

    @AllFieldsConstructor
    public MarkerAnnotationExpr(final Name name) {
        this(null, name);
    }

    /**
     * This constructor is used by the parser and is considered private.
     */
    @Generated("com.github.braisdom.jds.generator.core.node.MainConstructorGenerator")
    public MarkerAnnotationExpr(TokenRange tokenRange, Name name) {
        super(tokenRange, name);
        customInitialization();
    }

    @Override
    @Generated("com.github.braisdom.jds.generator.core.node.AcceptGenerator")
    public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
        return v.visit(this, arg);
    }

    @Override
    @Generated("com.github.braisdom.jds.generator.core.node.AcceptGenerator")
    public <A> void accept(final VoidVisitor<A> v, final A arg) {
        v.visit(this, arg);
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
    public MarkerAnnotationExpr clone() {
        return (MarkerAnnotationExpr) accept(new CloneVisitor(), null);
    }

    @Override
    @Generated("com.github.braisdom.jds.generator.core.node.GetMetaModelGenerator")
    public MarkerAnnotationExprMetaModel getMetaModel() {
        return JavaParserMetaModel.markerAnnotationExprMetaModel;
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
    public boolean isMarkerAnnotationExpr() {
        return true;
    }

    @Override
    @Generated("com.github.braisdom.jds.generator.core.node.TypeCastingGenerator")
    public MarkerAnnotationExpr asMarkerAnnotationExpr() {
        return this;
    }

    @Generated("com.github.braisdom.jds.generator.core.node.TypeCastingGenerator")
    public void ifMarkerAnnotationExpr(Consumer<MarkerAnnotationExpr> action) {
        action.accept(this);
    }

    @Override
    @Generated("com.github.braisdom.jds.generator.core.node.TypeCastingGenerator")
    public Optional<MarkerAnnotationExpr> toMarkerAnnotationExpr() {
        return Optional.of(this);
    }
}
