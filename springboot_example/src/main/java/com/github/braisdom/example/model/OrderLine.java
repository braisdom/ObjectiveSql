package com.github.braisdom.example.model;

import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.Relation;
import com.github.braisdom.objsql.relation.RelationType;

@DomainModel
public class OrderLine {
  
    private Long productId;
    private String barcode;
    private String orderNo;
    private Long memberId;
    private Long orderId;
    private Double salesPrice;
    private Double amount;
    private Double quantity;

    @Relation(relationType = RelationType.BELONGS_TO)
    private Order order;

    public OrderLine setOrder(Order order) {
        this.orderNo = order.getNo();
        this.orderId = order.getId();
        return this;
    }
}
