package com.github.braisdom.example.analysis;

import com.github.braisdom.example.model.Order;
import com.github.braisdom.example.model.OrderLine;
import com.github.braisdom.example.model.Product;
import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.DynamicModel;
import com.github.braisdom.objsql.DynamicQuery;
import com.github.braisdom.objsql.sql.*;

import java.sql.SQLException;
import java.util.List;

import static com.github.braisdom.objsql.sql.Expressions.column;
import static com.github.braisdom.objsql.sql.function.AnsiFunctions.*;
import static com.github.braisdom.objsql.sql.function.MySQLFunctions.toDateTime;

/**
 * The class is used for calculating
 */
public class ProductSales extends DynamicQuery<DynamicModel> {
    private static final String MYSQL_DATE_TIME_FORMAT = "%Y-%m-%d %H:%i:%s";

    private Select select;

    private Product.Table productTable = Product.asTable();
    private OrderSummary orderSummary = new OrderSummary();

    public ProductSales() {
        super(DatabaseType.MySQL);
        select = new Select();
    }

    public List<DynamicModel> execute(String dataSourceName) throws SQLException, SQLSyntaxException {
        select.project(
                productTable.barcode,
                productTable.name,
                orderSummary.memberCount,
                orderSummary.totalAmount,
                orderSummary.totalQuantity,
                orderSummary.salesPrice)
                .from(orderSummary.as("order"))
                .leftOuterJoin(productTable, productTable.id.eq(orderSummary.productId));
        return super.execute(DynamicModel.class, dataSourceName, select);
    }

    public ProductSales salesBetween(String begin, String end) {
        orderSummary.salesBetween(begin, end);
        return this;
    }

    public ProductSales productIn(String... barcodes) {
        orderSummary.productIn(barcodes);
        return this;
    }

    private class OrderSummary extends SubQuery {

        private final Order.Table orderTable = Order.asTable();
        private final OrderLine.Table orderLineTable = OrderLine.asTable();

        private Expression orderFilterExpression;

        public final Column productId = column(this, orderLineTable
                .productId.as("product_id"));
        public final Column memberCount = column(this,
                countDistinct(orderTable.memberId).as("member_count"));
        public final Column totalAmount = column(this,
                sumMoneyColumn(orderTable.amount).as("total_amount"));
        public final Column totalQuantity = column(this,
                sumMoneyColumn(orderTable.quantity).as("total_quantity"));
        public final Column salesPrice = column(this,
                avgMoneyColumn(orderLineTable.salesPrice).as("sales_price"));

        public OrderSummary() {
            project(productId, memberCount, totalAmount, totalQuantity, salesPrice)
                    .from(orderTable)
                    .leftOuterJoin(orderLineTable, orderLineTable.orderId.eq(orderTable.id))
                    .groupBy(orderLineTable.productId);
        }

        @Override
        public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
            where(orderFilterExpression);
            return super.toSql(expressionContext);
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
