package com.github.braisdom.objsql;

import com.github.braisdom.objsql.sql.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.github.braisdom.objsql.sql.Expressions.and;

public abstract class DynamicQuery<T extends DynamicModel> {

    private final DatabaseType databaseType;
    private final ExpressionContext expressionContext;

    protected DynamicQuery(DatabaseType databaseType) {
        this.databaseType = databaseType;
        this.expressionContext = new DefaultExpressionContext(databaseType);
    }

    protected List<T> execute(Class<T> clazz, String dataSourceName, Sqlizable sql)
            throws SQLException, SQLSyntaxException {
        Connection connection = Databases.getConnectionFactory().getConnection(dataSourceName);
        SQLExecutor<T> sqlExecutor = Databases.getSqlExecutor();

        return sqlExecutor.query(connection, sql.toSql(expressionContext),
                new DynamicTableRowDescriptor(clazz));
    }

    protected ExpressionContext getExpressionContext() {
        return expressionContext;
    }

    protected Expression appendAndExpression(Expression originalExpr, Expression newExpr) {
        if(originalExpr == null)
            return newExpr;
        return and(originalExpr, newExpr);
    }
}
