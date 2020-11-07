package com.github.braisdom.example.model;

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.Queryable;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import com.github.braisdom.objsql.sql.Select;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.SQLException;
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
     * Calculate LPLY and LP of products sales for a while.
     * <p>
     * SPLY: it means Same Period Last Year
     * LP: it means Last Period
     * </p>
     *
     * @return
     */
    public static List<Product> calProductSPLYAndLP(String calBegin, String calEnd) throws SQLSyntaxException, SQLException {
        DateTime begin = DateTime.parse(calBegin, DATE_TIME_FORMATTER);
        DateTime end = DateTime.parse(calEnd, DATE_TIME_FORMATTER);

        // Creating dataset of target, last period and same period last year
        Select target = createPeriodSales(calBegin, calEnd);
        Select lp = createPeriodSales(minusMonths(begin, 1), minusMonths(end, 1));
        Select sply = createPeriodSales(minusYears(begin, 1), minusYears(end, 1));

        Select select = new Select();
        select.from(target)
                .leftOuterJoin(lp, createLPJoinCondition(target, lp))
                .leftOuterJoin(sply, createSPLYJoinCondition(target, sply));

        // Create calculation expression of last period
        Expression lpAmount = createLPExpr(target, lp, "total_amount");
        Expression lpOrderCount = createLPExpr(target, lp, "order_count");
        Expression lpQuantity = createLPExpr(target, lp, "total_quantity");

        // Create calculation expression of same period last year
        Expression splyAmount = createSPLYExpr(target, lp, "total_amount");
        Expression splyOrderCount = createSPLYExpr(target, lp, "order_count");
        Expression splyQuantity = createSPLYExpr(target, lp, "total_quantity");

        select.project(target.col("barcode"))
                .project(target.col("sales_year"))
                .project(target.col("sales_month"))
                .project(format(lpAmount, 2).as("amount_lp"))
                .project(format(lpOrderCount, 2).as("order_count_lp"))
                .project(format(lpQuantity, 2).as("quantity_lp"))
                .project(format(splyAmount, 2).as("quantity_sply"))
                .project(format(splyOrderCount, 2).as("quantity_sply"))
                .project(format(splyQuantity, 2).as("quantity_sply"));

        select.groupBy(target.col("barcode"),
                target.col("sales_year"),
                target.col("sales_month"));

        return select.execute(DatabaseType.MySQL, Product.class);
    }

    private static Expression createLPExpr(Select target, Select select, String metricsName) {
        return (sum(target.col(metricsName))
                - sum(select.col(metricsName))) / sum(select.col(metricsName)) * $(100);
    }

    private static Expression createSPLYExpr(Select target, Select select, String metricsName) {
        return (sum(target.col(metricsName))
                - sum(select.col(metricsName))) / sum(select.col(metricsName)) * $(100);
    }

    private static String minusMonths(DateTime dateTime, int num) {
        return dateTime.minusMonths(num).toString(DATE_TIME_TEMPLATE);
    }

    private static String minusYears(DateTime dateTime, int num) {
        return dateTime.minusYears(num).toString(DATE_TIME_TEMPLATE);
    }

    private static Expression createLPJoinCondition(Select refDataset, Select lpDataset) {
        return eq(refDataset.col("sales_month"),
                plus(lpDataset.col("sales_month"), $(1)));
    }

    private static Expression createSPLYJoinCondition(Select refDataset, Select splyDataset) {
        return eq(refDataset.col("sales_month"), splyDataset.col("sales_month"));
    }

    private static Select createPeriodSales(String salesBegin, String salesEnd) {
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
}
