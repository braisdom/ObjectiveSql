package com.github.braisdom.example.statistics;

import com.github.braisdom.example.model.Member;
import com.github.braisdom.example.model.Order;
import com.github.braisdom.example.model.OrderLine;
import com.github.braisdom.example.model.Product;
import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.DynamicQuery;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import com.github.braisdom.objsql.sql.Select;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static com.github.braisdom.objsql.sql.function.MySQLFunctions.strToDate;

public class SalesSummary extends DynamicQuery<StatisticsObject> {
    private static final String MYSQL_DATE_TIME_FORMAT = "%Y-%m-%dT%H:%i:%s";

    private Timestamp begin;
    private Timestamp end;
    private String[] barcodes;
    private String[] members;

    private Expression whereExpression;
    private Select select;

    private Order.Table orderTable = Order.asTable();
    private Product.Table productTable = Product.asTable();
    private OrderLine.Table orderLineTable = OrderLine.asTable();
    private Member.Table memberTable = Member.asTable();

    public SalesSummary() {
        super(DatabaseType.MySQL);
        select = new Select();
    }

    public List<StatisticsObject> execute(String dataSourceName) throws SQLException, SQLSyntaxException {
        return super.execute(StatisticsObject.class, dataSourceName, select);
    }

    public SalesSummary salesBetween(Timestamp begin, Timestamp end) {
        whereExpression = appendAndExpression(whereExpression,
                orderTable.salesAt.between(strToDate(begin.toString(), MYSQL_DATE_TIME_FORMAT),
                        strToDate(end.toString(), MYSQL_DATE_TIME_FORMAT)));
        return this;
    }

    public SalesSummary salesBetween(String begin, String end) {
        whereExpression = appendAndExpression(whereExpression,
                orderTable.salesAt.between(strToDate(begin, MYSQL_DATE_TIME_FORMAT),
                        strToDate(end, MYSQL_DATE_TIME_FORMAT)));
        return this;
    }

    public SalesSummary productIn(String... barcodes) {
        whereExpression = appendAndExpression(whereExpression,
                productTable.barcode.in(barcodes));
        return this;
    }

    public SalesSummary memberIn(String... members) {
        whereExpression = appendAndExpression(whereExpression,
                productTable.barcode.in(members));
        return this;
    }
}
