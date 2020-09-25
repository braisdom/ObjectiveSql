package com.github.braisdom.example.analysis;

import com.github.braisdom.example.model.Order;
import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.DynamicModel;
import com.github.braisdom.objsql.DynamicQuery;
import com.github.braisdom.objsql.sql.*;
import com.github.braisdom.objsql.sql.expression.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.github.braisdom.objsql.sql.function.AnsiFunctions.sum;
import static com.github.braisdom.objsql.sql.function.MySQLFunctions.*;

public class SalesCompareTimeRelated extends DynamicQuery<DynamicModel> {

    private static final String MYSQL_DATE_TIME_FORMAT = "%Y-%m-%d %H:%i:%s";
    public static final String TIME_DIMENSION_MONTH = "month";
    public static final String TIME_DIMENSION_DAY = "day";

    private Order.Table orderTable = Order.asTable();

    private final List<Expression> groupByDimensions = new ArrayList<>();
    private final Select select;
    private String beginTime;
    private String endTime;
    private String timeDimension;

    public SalesCompareTimeRelated() {
        super(DatabaseType.MySQL);
        select = new Select();
    }



    public List<DynamicModel> execute(String dataSourceName) throws SQLSyntaxException, SQLException {
        String dateFormat;
        String interval;
        if (timeDimension.equals(TIME_DIMENSION_MONTH)) {
            dateFormat = "%Y-%m";
            interval = "INTERVAL 1 MONTH";
        } else if (timeDimension.equals(TIME_DIMENSION_DAY)) {
            dateFormat = "%Y-%m-%d";
            interval = "INTERVAL 1 DAY";
        } else {
            throw new SQLSyntaxException("The time dimension must be assigned, like day, month.");
        }

        SubQuery subQuery = compareSubQuery(dateFormat, null).as("a");
        SubQuery chainSubQuery = compareSubQuery(dateFormat, interval).as("b");
        SubQuery yoySubQuery = compareSubQuery(dateFormat, "INTERVAL 1 YEAR").as("c");

        select
                .project(subQuery.col("dt"),
                        subQuery.col("amount"),
                        chainSubQuery.col("dt").as("chain_dt"),
                        chainSubQuery.col("amount").as("chain_amount"),
                        generateCompareCase(subQuery, chainSubQuery).as("chain"),
                        yoySubQuery.col("dt").as("yoy_dt"),
                        yoySubQuery.col("amount").as("yoy_amount"),
                        generateCompareCase(subQuery, yoySubQuery).as("yoy")
                        )
                .from(subQuery)
                .leftOuterJoin(chainSubQuery, Expressions.eq(subQuery.col("dt"),
                        dateAdd(chainSubQuery.col("dt"), interval)))
                .leftOuterJoin(yoySubQuery, Expressions.eq(subQuery.col("dt"),
                        dateAddYear(yoySubQuery.col("dt"), 1)));

        return super.execute(DynamicModel.class, dataSourceName, select);
    }

    public SalesCompareTimeRelated timeBetween(String beginTime, String endTime) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        return this;
    }

    public SalesCompareTimeRelated timeDimension(String timeDimension) {
        this.timeDimension = timeDimension;
        return this;
    }

    public SalesCompareTimeRelated groupByDimension(String... columns) {
        if (columns != null) {
            for (String column : columns) {
                groupByDimensions.add(new PlainExpression(column));
            }
        }
        return this;
    }

    private Expression generateCompareCase(SubQuery query1, SubQuery query2) {
        CaseExpression caseExpression = new CaseExpression();
        caseExpression
                .when(new NullExpression(false, query1.col("amount")),
                        new LiteralExpression(0))
                .when(Expressions.or(new NullExpression(false, query2.col("amount")),
                        new EqualsExpression(false, query2.col("amount"), new LiteralExpression(0))),
                        new LiteralExpression(0))
                .sqlElse(Expressions.multiply(Expressions.divide(Expressions.minus(query1.col("amount"),
                        query2.col("amount")), query2.col("amount")), new LiteralExpression(100)));
        return caseExpression;
    }

    private SubQuery compareSubQuery(String dateFormat, String interval) {
        SubQuery subQuery = new SubQuery();

        List<Expression> selectExpressions = new ArrayList<>(groupByDimensions);
        selectExpressions.add(sum(orderTable.amount).as("amount"));
        selectExpressions.add(dateFormat(orderTable.salesAt, dateFormat).as("dt"));
        List<Expression> groupByExpressions = new ArrayList<>(groupByDimensions);
        groupByExpressions.add(dateFormat(orderTable.salesAt, dateFormat));

        subQuery
                .project(selectExpressions.toArray(new Expression[]{}))
                .from(orderTable)
                .where(generateTimeFilter(interval))
                .groupBy(groupByExpressions.toArray(new Expression[]{}));
        return subQuery;
    }

    private Expression generateTimeFilter(String interval) {
        if (interval == null)
            return orderTable.salesAt.between(strToDate(beginTime, MYSQL_DATE_TIME_FORMAT),
                    strToDate(endTime, MYSQL_DATE_TIME_FORMAT));

        return orderTable.salesAt.between(dateSub(strToDate(beginTime, MYSQL_DATE_TIME_FORMAT), interval),
                dateSub(strToDate(endTime, MYSQL_DATE_TIME_FORMAT), interval));
    }
}
