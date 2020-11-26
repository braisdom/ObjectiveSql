package com.github.braisdom.objsql.pagination.impl;

import com.github.braisdom.objsql.DomainModelDescriptor;
import com.github.braisdom.objsql.pagination.Page;
import com.github.braisdom.objsql.pagination.PagedSQLBuilder;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.sql.SQLException;
import java.util.Arrays;

public class MsSQLServerPagedSQLBuilder implements PagedSQLBuilder {

    @Override
    public String buildQuerySQL(Page page, String rawSQL, DomainModelDescriptor modelDescriptor) throws SQLException {
        try {
            Statement statement = CCJSqlParserUtil.parse(rawSQL);
            Select originalSelect = (Select) statement;
            PlainSelect plainSelect = (PlainSelect) originalSelect.getSelectBody();
            Offset offset = new Offset();
            Fetch fetch = new Fetch();

            offset.setOffset(page.getOffset());
            offset.setOffsetParam("ROWS");
            fetch.setRowCount(page.getPageSize());

            if (plainSelect.getOrderByElements() == null || plainSelect.getOrderByElements().size() == 0) {
                OrderByElement orderByElement = new OrderByElement();

                orderByElement.setExpression(new Column(modelDescriptor.getPrimaryKey().name()));
                orderByElement.setAsc(true);

                plainSelect.setOrderByElements(Arrays.asList(orderByElement));
            }

            plainSelect.setOffset(offset);
            plainSelect.setFetch(fetch);

            return originalSelect.toString();
        } catch (JSQLParserException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }
}
