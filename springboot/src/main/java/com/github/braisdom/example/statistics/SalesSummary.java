package com.github.braisdom.example.statistics;

import com.github.braisdom.example.model.Member;
import com.github.braisdom.example.model.Order;
import com.github.braisdom.example.model.OrderLine;
import com.github.braisdom.example.model.Product;
import com.github.braisdom.objsql.sql.Select;

import java.sql.Timestamp;

public class SalesSummary {

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
        select = new Select();
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
