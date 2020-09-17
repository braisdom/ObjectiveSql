package com.github.braisdom.example.model;

import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.Relation;
import com.github.braisdom.objsql.relation.RelationType;

@DomainModel
public class OrderLine {
    private String barcode;
    private String orderNo;
    private Integer orderId;
    private Float amount;
    private Float quantity;

    @Relation(relationType = RelationType.BELONGS_TO)
    private Order order;

    public OrderLine setOrder(Order order) {
        this.orderNo = order.getNo();
        this.orderId = order.getId();
        return this;
    }
}
