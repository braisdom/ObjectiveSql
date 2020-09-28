package com.github.braisdom.example.analysis;

import com.github.braisdom.example.model.Order;
import com.github.braisdom.example.model.OrderLine;
import com.github.braisdom.example.model.Product;
import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.DynamicModel;
import com.github.braisdom.objsql.DynamicQuery;
import com.github.braisdom.objsql.sql.*;
import com.sun.tools.corba.se.idl.constExpr.Or;

import java.sql.SQLException;
import java.util.List;

import static com.github.braisdom.objsql.sql.function.AnsiFunctions.*;
import static com.github.braisdom.objsql.sql.function.MySQLFunctions.toDateTime;

/**
 * The class is used for calculating
 */
public class ProductSales extends DynamicQuery<DynamicModel> {
    private static final String MYSQL_DATE_TIME_FORMAT = "%Y-%m-%d %H:%i:%s";

    private Expression orderFilterExpression;
    private Select select;

    private Order.Table orderTable = Order.asTable();
    private Product.Table productTable = Product.asTable();
    private OrderLine.Table orderLineTable = OrderLine.asTable();

    public ProductSales() {
        super(DatabaseType.MySQL);
        select = new Select();
    }

    public List<DynamicModel> execute(String dataSourceName) throws SQLException, SQLSyntaxException {
        if (orderFilterExpression == null)
            throw new SQLSyntaxException("The order filter expression must be given");

        final SubQuery orderQuery = createOrderSummary();
        select.project(productTable.barcode, productTable.name,
                orderQuery.getProjection("member_count"),
                orderQuery.getProjection("total_amount"),
                orderQuery.getProjection("total_quantity"),
                orderQuery.getProjection("sales_price"))
                .from(orderQuery.as("order"))
                .leftOuterJoin(productTable, productTable.id.eq(orderQuery.col("product_id")));
        return super.execute(DynamicModel.class, dataSourceName, select);
    }

    private Expression sumMoneyColumn(Column column) {
        return round(sum(column), 2);
    }

    private Expression avgMoneyColumn(Column column) {
        return round(avg(column), 2);
    }

    private SubQuery createOrderSummary() {
        final SubQuery orderSummary = new SubQuery();

        orderSummary.project(
                orderLineTable.productId.as("product_id"),
                countDistinct(orderTable.memberId).as("member_count"),
                sumMoneyColumn(orderTable.amount).as("total_amount"),
                sumMoneyColumn(orderTable.quantity).as("total_quantity"),
                avgMoneyColumn(orderLineTable.salesPrice).as("sales_price"))
                .from(orderTable)
                .where(orderFilterExpression)
                .leftOuterJoin(orderLineTable, orderLineTable.orderId.eq(orderTable.id))
                .groupBy(orderLineTable.productId);

        return orderSummary;
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

    private class OrderSummary extends SubQuery {

        private final Order.Table orderTable = Order.asTable();
        private final OrderLine.Table orderLineTable = OrderLine.asTable();

        private Expression orderFilterExpression;

        public OrderSummary(Expression orderFilterExpression) {
            setupProjection();
        }

        @Override
        public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
            where(orderFilterExpression);

            return super.toSql(expressionContext);
        }

        private void setupProjection() {

        }

        public void salesBetween(String begin, String end) {
            orderFilterExpression = appendAndExpression(orderFilterExpression,
                    orderTable.salesAt.between(toDateTime(begin), toDateTime(end)));
        }

        public void productIn(String... barcodes) {
            orderFilterExpression = appendAndExpression(orderFilterExpression,
                    orderLineTable.barcode.in(barcodes));
        }

        private Expression sumMoneyColumn(Column column) {
            return round(sum(column), 2);
        }

        private Expression avgMoneyColumn(Column column) {
            return round(avg(column), 2);
        }
    }

    public static void main(String[] args) throws SQLSyntaxException, SQLException {
        ProductSales productSales = new ProductSales();

        productSales.salesBetween("2020-09-01 00:00:00", "2020-09-10 00:00:00")
                .productIn("P2020000018", "P202000007", "P2020000011");

        productSales.execute(Databases.getDefaultDataSourceName());
    }
}
