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

        // Dataset of target data set
        Select target = getPeriodSales(calBegin, calEnd);

        // Dataset of last period
        Select lp = getPeriodSales(minusMonths(begin, 1), minusMonths(end, 1));

        // Dataset of same period last year
        Select sply = getPeriodSales(minusYears(begin, 1), minusYears(end, 1));

        Select select = new Select();
        select.from(target)
                .leftOuterJoin(lp, createLPJoinCondition(target, lp))
                .leftOuterJoin(sply, createSPLYJoinCondition(target, sply));

        Expression lpAmount = (sum(target.col("total_amount"))
                - sum(lp.col("total_amount"))) / sum(lp.col("total_amount")) * $(100);
        Expression lpOrderCount = (sum(target.col("order_count"))
                - sum(lp.col("order_count"))) / sum(lp.col("order_count")) * $(100);
        Expression lpQuantity = (sum(target.col("total_quantity"))
                - sum(lp.col("total_quantity"))) / sum(lp.col("total_quantity")) * $(100);

        Expression splyAmount = (sum(target.col("total_amount"))
                - sum(sply.col("total_amount"))) / sum(sply.col("total_amount")) * $(100);
        Expression splyOrderCount = (sum(target.col("order_count"))
                - sum(sply.col("order_count"))) / sum(sply.col("order_count")) * $(100);
        Expression splyQuantity = (sum(target.col("total_quantity"))
                - sum(sply.col("total_quantity"))) / sum(sply.col("total_quantity")) * $(100);

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

    private static Select getPeriodSales(String salesBegin, String salesEnd) {
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
