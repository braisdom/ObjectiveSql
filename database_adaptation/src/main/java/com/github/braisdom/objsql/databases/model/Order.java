package com.github.braisdom.objsql.databases.model;

import com.github.braisdom.objsql.annotations.Column;
import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.Relation;
import com.github.braisdom.objsql.relation.RelationType;
import com.github.braisdom.objsql.transition.SqlDateTimeTransitional;

import java.sql.Timestamp;
import java.util.List;

@DomainModel
public class Order {

    private static final String KEY_ORDER_LINES = "orderLines";

    private String no;
    private Integer memberId;
    private Double amount;
    private Double quantity;

    @Column(transition = SqlDateTimeTransitional.class)
    private Timestamp salesAt;

    @Relation(relationType = RelationType.BELONGS_TO)
    private Member member;

    @Relation(relationType = RelationType.HAS_MANY)
    private List<OrderLine> orderLines;

}
