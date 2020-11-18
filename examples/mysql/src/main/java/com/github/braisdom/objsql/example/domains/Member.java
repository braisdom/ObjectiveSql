package com.github.braisdom.objsql.example.domains;

import com.github.braisdom.objsql.annotations.*;
import com.github.braisdom.objsql.example.transition.JsonColumnTransition;
import com.github.braisdom.objsql.relation.RelationType;
import com.github.braisdom.objsql.transition.SqlDateTimeTransition;
import org.joda.time.DateTime;

import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@DomainModel(primaryClass = Integer.class)
public class Member {
    @Size(min = 5, max = 20)
    private String no;

    @Queryable
    private String name;

    @Queryable
    private Integer gender;
    private String mobile;

    @Relation(relationType = RelationType.HAS_MANY)
    private List<Order> orders;

    @Column(transition = JsonColumnTransition.class)
    private Map extendedAttributes;

    @Column(transition = SqlDateTimeTransition.class)
    private Timestamp registeredAt;

    @Column(transition = SqlDateTimeTransition.class)
    private Timestamp updatedAt;

    @Transient
    private String otherInfo;

    public Member setRegisteredAtWithJoda(DateTime dateTime) {
        registeredAt = new Timestamp(dateTime.getMillis());
        return this;
    }
}
