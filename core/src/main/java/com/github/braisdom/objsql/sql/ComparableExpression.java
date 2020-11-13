package com.github.braisdom.objsql.sql;

import static com.github.braisdom.objsql.sql.Expressions.$;

public interface ComparableExpression extends Sqlizable {

    LogicalExpression lt(Expression expr);

    default LogicalExpression lt(Long literal) {
        return lt($(literal));
    }

    default LogicalExpression lt(Integer literal) {
        return lt($(literal));
    }

    default LogicalExpression lt(Short literal) {
        return lt($(literal));
    }

    default LogicalExpression lt(Byte literal) {
        return lt($(literal));
    }

    default LogicalExpression lt(Double literal) {
        return lt($(literal));
    }

    default LogicalExpression lt(Float literal) {
        return lt($(literal));
    }

    LogicalExpression gt(Expression expr);

    default LogicalExpression gt(Long literal) {
        return gt($(literal));
    }

    default LogicalExpression gt(Integer literal) {
        return gt($(literal));
    }

    default LogicalExpression gt(Short literal) {
        return gt($(literal));
    }

    default LogicalExpression gt(Byte literal) {
        return gt($(literal));
    }

    default LogicalExpression gt(Double literal) {
        return gt($(literal));
    }

    default LogicalExpression gt(Float literal) {
        return gt($(literal));
    }

    LogicalExpression eq(Expression expr);

    default LogicalExpression eq(Long literal) {
        return eq($(literal));
    }

    default LogicalExpression eq(Integer literal) {
        return eq($(literal));
    }

    default LogicalExpression eq(Short literal) {
        return eq($(literal));
    }

    default LogicalExpression eq(Byte literal) {
        return eq($(literal));
    }

    default LogicalExpression eq(Double literal) {
        return eq($(literal));
    }

    default LogicalExpression eq(Float literal) {
        return eq($(literal));
    }

    LogicalExpression le(Expression expr);

    default LogicalExpression le(Long literal) {
        return le($(literal));
    }

    default LogicalExpression le(Integer literal) {
        return le($(literal));
    }

    default LogicalExpression le(Short literal) {
        return le($(literal));
    }

    default LogicalExpression le(Byte literal) {
        return le($(literal));
    }

    default LogicalExpression le(Double literal) {
        return le($(literal));
    }

    default LogicalExpression le(Float literal) {
        return le($(literal));
    }

    LogicalExpression ge(Expression expr);

    default LogicalExpression ge(Long literal) {
        return ge($(literal));
    }

    default LogicalExpression ge(Integer literal) {
        return ge($(literal));
    }

    default LogicalExpression ge(Short literal) {
        return ge($(literal));
    }

    default LogicalExpression ge(Byte literal) {
        return ge($(literal));
    }

    default LogicalExpression ge(Double literal) {
        return ge($(literal));
    }

    default LogicalExpression ge(Float literal) {
        return ge($(literal));
    }

    LogicalExpression ne(Expression expr);

    default LogicalExpression ne(Long literal) {
        return ne($(literal));
    }

    default LogicalExpression ne(Integer literal) {
        return ne($(literal));
    }

    default LogicalExpression ne(Short literal) {
        return ne($(literal));
    }

    default LogicalExpression ne(Byte literal) {
        return ne($(literal));
    }

    default LogicalExpression ne(Double literal) {
        return ne($(literal));
    }

    default LogicalExpression ne(Float literal) {
        return ne($(literal));
    }

    LogicalExpression ne2(Expression expr);

    default LogicalExpression ne2(Long literal) {
        return ne($(literal));
    }

    default LogicalExpression ne2(Integer literal) {
        return ne($(literal));
    }

    default LogicalExpression ne2(Short literal) {
        return ne($(literal));
    }

    default LogicalExpression ne2(Byte literal) {
        return ne($(literal));
    }

    default LogicalExpression ne2(Double literal) {
        return ne($(literal));
    }

    default LogicalExpression ne2(Float literal) {
        return ne($(literal));
    }

}
