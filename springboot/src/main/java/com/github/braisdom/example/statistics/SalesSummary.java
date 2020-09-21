package com.github.braisdom.example.statistics;

import com.github.braisdom.example.model.Member;
import com.github.braisdom.example.model.Order;
import com.github.braisdom.example.model.OrderLine;
import com.github.braisdom.example.model.Product;
import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.DynamicQuery;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import com.github.braisdom.objsql.sql.Select;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class SalesSummary extends DynamicQuery<StatisticsObject> {

    private Timestamp begin;
    private Timestamp end;
    private String[] barcodes;
    private String[] members;

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
        this.begin = begin;
        this.end = end;
        return this;
    }

    public SalesSummary salesBetween(String begin, String end) {
        this.begin = Timestamp.valueOf(begin);
        this.end = Timestamp.valueOf(end);
        return this;
    }

    public SalesSummary productIn(String... barcodes) {
        this.barcodes = barcodes;
        return this;
    }

    public SalesSummary memberIn(String... members) {
        this.members = members;
        return this;
    }
}
