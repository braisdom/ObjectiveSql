package com.github.braisdom.example.model;

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.Queryable;
import com.github.braisdom.objsql.sql.DefaultExpressionContext;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import com.github.braisdom.objsql.sql.Select;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import static com.github.braisdom.objsql.sql.Expressions.*;
import static com.github.braisdom.objsql.sql.function.Ansi.countDistinct;
import static com.github.braisdom.objsql.sql.function.Ansi.sum;
import static com.github.braisdom.objsql.sql.function.MySQL.*;

@DomainModel
public class Product {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:SS");
    private static final String DATE_TIME_TEMPLATE = "YYYY-MM-dd HH:mm";

    @Queryable
    private String barcode;
    @Queryable
    private String name;
    private Integer categoryId;
    private Float salesPrice;
    private Float cost;

    /**
     * Calculate Same Period Last Year of products sales.
     * <p>
     * SPLY: it means Same Period Last Year
     * LP: it means Last Period
     *
     * @return
     */
    public static List<Product> calProductSPLYAndLP(String calBegin, String calEnd) throws SQLSyntaxException, SQLSyntaxException {
        DateTime calBeginDateTime = DateTime.parse(calBegin, DATE_TIME_FORMATTER);
        DateTime calEndDateTime = DateTime.parse(calEnd, DATE_TIME_FORMATTER);

        // Target data set
        Select refDataset = getPeriodProductsSales(calBegin, calEnd);

        // Last period
        Select lpDataset = getPeriodProductsSales(
                calBeginDateTime.minusMonths(1).toString(DATE_TIME_TEMPLATE),
                calEndDateTime.minusMonths(1).toString(DATE_TIME_TEMPLATE));

        // Same period last year
        Select splyDataset = getPeriodProductsSales(
                calBeginDateTime.minusYears(1).toString(DATE_TIME_TEMPLATE),
                calEndDateTime.minusYears(1).toString(DATE_TIME_TEMPLATE));

        Select select = new Select();
        select.from(refDataset)
                .leftOuterJoin(lpDataset, eq(refDataset.col("sales_month"),
                        plus(lpDataset.col("sales_month"), $(1))))
                .leftOuterJoin(splyDataset, eq(refDataset.col("sales_month"),
                        splyDataset.col("sales_month")));

        Expression lpAmount = multiply(
                divide(
                        minus(
                                sum(refDataset.col("total_amount")),
                                sum(lpDataset.col("total_amount"))
                        ),
                        sum(lpDataset.col("total_amount"))
                ), $(100)
        );

        select.project(refDataset.col("barcode"))
                .project(refDataset.col("sales_year"))
                .project(refDataset.col("sales_month"))
                .project(format(lpAmount, 2).as("amount_lp"));

        select.groupBy(refDataset.col("barcode"),
                refDataset.col("sales_year"),
                refDataset.col("sales_month"));

        String sql = select.toSql(new DefaultExpressionContext(DatabaseType.MySQL));
        return null;
    }

    private static Select getPeriodProductsSales(String salesBegin, String salesEnd) {
        Order.Table orderTable = Order.asTable();
        OrderLine.Table orderLineTable = OrderLine.asTable();

        Select select = new Select();

        select.from(orderLineTable)
                .leftOuterJoin(orderTable, orderLineTable.orderId.eq(orderTable.id))
                .groupBy(orderLineTable.barcode, year(orderTable.salesAt), month(orderTable.salesAt))
                .where(orderTable.salesAt.between($(salesBegin), $(salesEnd)));

        select.project(orderLineTable.barcode.as("barcode"))
                .project(year(orderTable.salesAt).as("sales_year"))
                .project(month(orderTable.salesAt).as("sales_month"))
                .project(countDistinct(orderLineTable.orderNo).as("order_count"))
                .project(sum(orderLineTable.amount).as("total_amount"))
                .project(sum(orderLineTable.quantity).as("total_quantity"));

        return select;
    }

    public static void main(String args[]) throws SQLSyntaxException {
        calProductSPLYAndLP("2020-09-01 00:00:00", "2020-11-30 23:59:59");
    }
}
