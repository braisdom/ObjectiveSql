package com.github.braisdom.objsql;

import com.github.braisdom.objsql.sql.DefaultExpressionContext;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import com.github.braisdom.objsql.sql.Sqlizable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.github.braisdom.objsql.sql.Expressions.and;

public abstract class DynamicQuery<T> {

    private final DatabaseType databaseType;

    protected DynamicQuery(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    protected List<T> execute(Class<T> clazz, String dataSourceName, Sqlizable sql)
            throws SQLException, SQLSyntaxException {
        Connection connection = Databases.getConnectionFactory().getConnection(dataSourceName);
        SQLExecutor<T> sqlExecutor = Databases.getSqlExecutor();

        return sqlExecutor.query(connection, sql.toSql(new DefaultExpressionContext(databaseType)),
                new DynamicTableRowDescriptor(clazz));
    }

    protected Expression appendAndExpression(Expression originalExpr, Expression newExpr) {
        if(originalExpr == null)
            return newExpr;
        return and(originalExpr, newExpr);
    }
}
