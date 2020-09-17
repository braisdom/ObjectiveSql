package com.github.braisdom.example.model;

import com.github.braisdom.example.RawObject;
import com.github.braisdom.objsql.annotations.Column;
import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.Relation;
import com.github.braisdom.objsql.annotations.Transactional;
import com.github.braisdom.objsql.relation.RelationType;
import com.github.braisdom.objsql.transition.SqlDateTimeTransitional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@DomainModel
public class Order {

    private static final String RAW_OBJECT_KEY_ORDER_LINES = "orderLines";

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
    public static Order makeOrder(Member member, RawObject rawOrder) throws SQLException {
        List<Map<String, Object>> rawOrderLines = rawOrder.get(List.class, RAW_OBJECT_KEY_ORDER_LINES);

        Order dirtyOrder = Order.newInstanceFrom(rawOrder, false);
        dirtyOrder.setMemberId(member.getId());

        Order order = dirtyOrder.save(true);
        return null;
    }
}
