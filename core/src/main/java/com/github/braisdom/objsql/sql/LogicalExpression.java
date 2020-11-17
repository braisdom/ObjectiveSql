package com.github.braisdom.objsql.sql;

public interface LogicalExpression extends Sqlizable {

    LogicalExpression and(LogicalExpression logicalExpression);

    LogicalExpression or(LogicalExpression logicalExpression);
}
