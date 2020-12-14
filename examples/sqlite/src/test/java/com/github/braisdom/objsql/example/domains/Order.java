package com.github.braisdom.objsql.example.domains;

import com.github.braisdom.objsql.annotations.Column;
import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.Relation;
import com.github.braisdom.objsql.annotations.Transactional;
import com.github.braisdom.objsql.relation.RelationType;
import com.github.braisdom.objsql.transition.SqlDateTimeTransition;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@DomainModel(primaryClass = Integer.class)
public class Order {
    private String no;
    private Integer memberId;
    private Double amount;
    private Double quantity;

    @Column(transition = SqlDateTimeTransition.class)
    private Timestamp salesAt;

    @Relation(relationType = RelationType.BELONGS_TO)
    private Member member;

    @Relation(relationType = RelationType.HAS_MANY)
    private List<OrderLine> orderLines;

    @Transactional
    public static void makeOrder(Order order, OrderLine... orderLines) throws SQLException {
        Order.create(order, false);
        OrderLine.create(orderLines, false);
    }
}
