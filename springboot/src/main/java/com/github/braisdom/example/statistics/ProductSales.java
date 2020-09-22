package com.github.braisdom.example.statistics;

import com.github.braisdom.example.model.Member;
import com.github.braisdom.example.model.Order;
import com.github.braisdom.example.model.OrderLine;
import com.github.braisdom.example.model.Product;
import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.DynamicQuery;
import com.github.braisdom.objsql.sql.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static com.github.braisdom.objsql.sql.function.AnsiFunctions.*;
import static com.github.braisdom.objsql.sql.function.MySQLFunctions.strToDate;
import static com.github.braisdom.objsql.sql.function.MySQLFunctions.toDateTime;

public class ProductSales extends DynamicQuery<StatisticsObject> {
    private static final String MYSQL_DATE_TIME_FORMAT = "%Y-%m-%d %H:%i:%s";

    private Expression orderFilterExpression;
    private Expression globalFilterExpression;
    private Select select;

    private Order.Table orderTable = Order.asTable();
    private Product.Table productTable = Product.asTable();
    private OrderLine.Table orderLineTable = OrderLine.asTable();
    private Member.Table memberTable = Member.asTable();

    public ProductSales() {
        super(DatabaseType.MySQL);
        select = new Select();
    }

    public List<StatisticsObject> execute(String dataSourceName) throws SQLException, SQLSyntaxException {
        if (orderFilterExpression == null)
            throw new SQLSyntaxException("The order filter expression must be given");
        if (globalFilterExpression == null)
            throw new SQLSyntaxException("The where filter expression must be given");

        final SubQuery orderQuery = createOrderSummary();

        select.from(orderQuery.as("order"))
                .where(globalFilterExpression)
                .leftOuterJoin(productTable, productTable.id.eq(orderQuery.col("product_id")));
        final String sql = select.toSql(new DefaultExpressionContext(DatabaseType.MySQL));
        return super.execute(StatisticsObject.class, dataSourceName, select);
    }

    private SubQuery createOrderSummary() {
        final SubQuery orderSummary = new SubQuery();

        orderSummary
                .project(orderLineTable.productId.as("product_id"),
                        countDistinct(orderTable.memberId).as("member_count"),
                        sum(orderTable.amount).as("total_amount"),
                        sum(orderTable.quantity).as("total_quantity"),
                        avg(orderLineTable.salesPrice).as("sales_price"))
                .from(orderTable)
                .where(orderFilterExpression)
                .leftOuterJoin(orderLineTable, orderLineTable.orderId.eq(orderTable.id))
                .groupBy(orderLineTable.productId);

        return orderSummary;
    }

    public ProductSales salesBetween(Timestamp begin, Timestamp end) {
        orderFilterExpression = appendAndExpression(orderFilterExpression,
                orderTable.salesAt.between(strToDate(begin.toString(), MYSQL_DATE_TIME_FORMAT),
                        strToDate(end.toString(), MYSQL_DATE_TIME_FORMAT)));
        return this;
    }

    public ProductSales salesBetween(String begin, String end) {
        orderFilterExpression = appendAndExpression(orderFilterExpression,
                orderTable.salesAt.between(toDateTime(begin), toDateTime(end)));
        return this;
    }

    public ProductSales productIn(String... barcodes) {
        orderFilterExpression = appendAndExpression(orderFilterExpression,
                orderLineTable.barcode.in(barcodes));
        return this;
    }

    public ProductSales memberNoIn(String... memberNos) {
        globalFilterExpression = appendAndExpression(globalFilterExpression,
                memberTable.no.in(memberNos));
        return this;
    }

    public static void main(String[] args) throws SQLSyntaxException, SQLException {
        ProductSales productSales = new ProductSales();

        productSales.salesBetween("2019-01-01 00:00:00", "2019-02-01 00:00:00")
                .productIn("11111", "2222")
                .memberNoIn("001", "001");

        productSales.execute(Databases.getDefaultDataSourceName());
    }
}
