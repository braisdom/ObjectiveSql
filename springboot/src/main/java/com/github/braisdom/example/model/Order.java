package com.github.braisdom.example.model;

import com.github.braisdom.example.RequestObject;
import com.github.braisdom.objsql.annotations.Column;
import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.Relation;
import com.github.braisdom.objsql.annotations.Transactional;
import com.github.braisdom.objsql.relation.RelationType;
import com.github.braisdom.objsql.transition.SqlDateTimeTransitional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@DomainModel
public class Order {

    private static final String KEY_ORDER_LINES = "orderLines";

    private String no;
    private Integer memberId;
    private Float amount;
    private Float quantity;

    @Column(transition = SqlDateTimeTransitional.class)
    private Timestamp salesAt;

    @Relation(relationType = RelationType.BELONGS_TO)
    private Member member;

    @Relation(relationType = RelationType.HAS_MANY)
    private List<OrderLine> orderLines;

    @Transactional
    public static Order makeOrder(Member member, RequestObject rawOrder) throws SQLException {
        RequestObject[] rawOrderLines = rawOrder.getArray(KEY_ORDER_LINES);

        Order dirtyOrder = Order.newInstanceFrom(rawOrder, false);
        dirtyOrder.setMemberId(member.getId());

        Order order = dirtyOrder.save(true);
        List<OrderLine> orderLines = new ArrayList();
        for (RequestObject rawOrderLine : rawOrderLines) {
            OrderLine orderLine = OrderLine.newInstanceFrom(rawOrderLine, false);
            orderLine.setOrder(order);
        }

        OrderLine.create(orderLines.toArray(new OrderLine[]{}), false);
        return order;
    }
}
