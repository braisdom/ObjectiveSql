package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.util.StringUtil;

import java.util.Objects;

public class GeneralSQLGenerator implements SQLGenerator {

    private static final String SELECT_STATEMENT = "SELECT %s FROM %s";
    private static final String UPDATE_STATEMENT = "UPDATE %s SET %s WHERE %s";
    private static final String DELETE_STATEMENT = "DELETE FROM %s WHERE %s";

    @Override
    public String createQuerySQL(String tableName, String projections, String filter) {
        return createQuerySQL(tableName, projections, filter, null, null, null, -1, -1);
    }

    @Override
    public String createQuerySQL(String tableName, String projections, String filter, String groupBy,
                                 String having, String orderBy, int offset, int limit) {
        Objects.requireNonNull(tableName, "The tableName cannot be null");

        StringBuilder sql = new StringBuilder();

        projections = (projections == null || projections.length() < 0) ? "*" : projections;
        String standardSql = String.format(SELECT_STATEMENT, projections, tableName);

        sql.append(standardSql);

        if(!StringUtil.isBlank(filter))
            sql.append(" WHERE ").append(filter);

        if(!StringUtil.isBlank(groupBy))
            sql.append(" GROUP BY ").append(groupBy);

        if(!StringUtil.isBlank(having))
            sql.append(" HAVING ").append(having);

        if(!StringUtil.isBlank(orderBy))
            sql.append(" ORDER BY ").append(orderBy);

        if(offset > 0)
            sql.append(" OFFSET ").append(offset);

        if(limit > 0)
            sql.append(" LIMIT ").append(limit);

        return sql.toString();
    }

    @Override
    public String createUpdateSQL(String tableName, String update, String filter) {
        Objects.requireNonNull(tableName, "The tableName cannot be null");

        if (StringUtil.isBlank(update))
            throw new NullPointerException("The columns for updating is required");

        if (StringUtil.isBlank(filter))
            throw new NullPointerException("The filter cannot be null when updating");

        return String.format(UPDATE_STATEMENT, tableName, update, filter);
    }

    @Override
    public String createDeleteSQL(String tableName, String filter) {
        Objects.requireNonNull(tableName, "The tableName cannot be null");

        if (StringUtil.isBlank(filter))
            throw new NullPointerException("The filter cannot be null when deleting");

        return String.format(DELETE_STATEMENT, tableName, filter);
    }

    @Override
    public String quote(Object... scalars) {
        StringBuilder sb = new StringBuilder();

        for (Object value : scalars) {
            if (value instanceof Integer || value instanceof Long ||
                    value instanceof Float || value instanceof Double)
                sb.append(String.valueOf(value));
            else
                sb.append(String.format("'%s'", String.valueOf(value)));
            sb.append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }
}
