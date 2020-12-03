package com.github.braisdom.objsql.sample.model;

import com.github.braisdom.objsql.annotations.*;
import com.github.braisdom.objsql.relation.RelationType;
import com.github.braisdom.objsql.sample.RequestObject;
import com.github.braisdom.objsql.transition.SqlDateTimeTransition;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@DomainModel
public class Order {
    private static final String KEY_ORDER_LINES = "orderLines";

    @Queryable
    private String no;
    private Long memberId;
    private Float amount;
    private Float quantity;

    @Column(transition = SqlDateTimeTransition.class)
    private Timestamp salesAt;

    @Relation(relationType = RelationType.BELONGS_TO)
    private Member member;

    @Relation(relationType = RelationType.HAS_MANY)
    private List<OrderLine> orderLines;

    public static List<Order> queryOrders(String begin, String end, String[] memberNos) {
        return null;
    }

    @Transactional
    public static Order makeOrder(Member member, RequestObject rawOrder) throws SQLException {
        RequestObject[] rawOrderLines = rawOrder.getArray(KEY_ORDER_LINES);

        Order dirtyOrder = Order.newInstanceFrom(rawOrder);
        dirtyOrder.setMemberId(member.getId());

        Order order = dirtyOrder.save(true);

        List<OrderLine> orderLines = new ArrayList();
        for (RequestObject rawOrderLine : rawOrderLines) {
            OrderLine orderLine = OrderLine.newInstanceFrom(rawOrderLine);

            orderLine.setOrder(order);
            orderLine.setMemberId(member.getId());

            orderLines.add(orderLine);
        }

        OrderLine.create(orderLines.toArray(new OrderLine[]{}), false);
        return order;
    }
}
