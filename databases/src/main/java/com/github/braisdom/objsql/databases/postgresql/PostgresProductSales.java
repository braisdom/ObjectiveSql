package com.github.braisdom.objsql.databases.postgresql;

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.DynamicModel;
import com.github.braisdom.objsql.DynamicQuery;
import com.github.braisdom.objsql.databases.model.Member;
import com.github.braisdom.objsql.databases.model.Order;
import com.github.braisdom.objsql.databases.model.OrderLine;
import com.github.braisdom.objsql.databases.model.Product;
import com.github.braisdom.objsql.sql.*;

import java.sql.SQLException;
import java.util.List;

import static com.github.braisdom.objsql.sql.function.AnsiFunctions.*;
import static com.github.braisdom.objsql.sql.function.AnsiFunctions.countDistinct;
import static com.github.braisdom.objsql.sql.function.PostgreSql.toDateTime;

public class PostgresProductSales extends DynamicQuery<DynamicModel> {
    private Expression orderFilterExpression;
    private Select select;

    private Order.Table orderTable = Order.asTable();
    private Product.Table productTable = Product.asTable();
    private OrderLine.Table orderLineTable = OrderLine.asTable();
    private Member.Table memberTable = Member.asTable();

    public PostgresProductSales() {
        super(DatabaseType.PostgreSQL);
        select = new Select();
    }

    public List<DynamicModel> execute(String dataSourceName) throws SQLException, SQLSyntaxException {
        if (orderFilterExpression == null)
            throw new SQLSyntaxException("The order filter expression must be given");

        final SubQuery orderQuery = createOrderSummary();
        select.project(productTable.barcode,
                productTable.name,
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
                avgMoneyColumn(orderLineTable.salesPrice).as("sales_price")
        )
                .from(orderTable)
                .where(orderFilterExpression)
                .leftOuterJoin(orderLineTable, orderLineTable.orderId.eq(orderTable.id))
                .groupBy(orderLineTable.productId);

        return orderSummary;
    }

    public PostgresProductSales salesBetween(String begin, String end) {
        orderFilterExpression = appendAndExpression(orderFilterExpression,
                orderTable.salesAt.between(toDateTime(begin), toDateTime(end)));
        return this;
    }

    public PostgresProductSales productIn(String... barcodes) {
        orderFilterExpression = appendAndExpression(orderFilterExpression,
                orderLineTable.barcode.in(barcodes));
        return this;
    }
}
