package com.github.braisdom.objsql.sample.model;

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
    private Float salesPrice;
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
