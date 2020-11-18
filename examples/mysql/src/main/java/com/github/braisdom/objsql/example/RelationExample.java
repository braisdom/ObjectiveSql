package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.example.domains.Member;
import com.github.braisdom.objsql.example.domains.Order;
import com.github.braisdom.objsql.relation.Relationship;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RelationExample extends MySQLExample {

    private static final String[] MEMBER_NAMES = {"Joe", "Juan", "Jack", "Albert", "Jonathan", "Justin", "Terry"};

    @Test
    public void prepareRelationData() throws SQLException {
        List<Member> members = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            members.add(new Member()
                    .setId((long) i)
                    .setNo("Q200000" + i)
                    .setName(MEMBER_NAMES[i])
                    .setGender(0)
                    .setMobile("150000000" + i));
        }

        for (int i = 0; i < 100; i++) {
            orders.add(new Order()
                    .setId((long) i)
                    .setNo("20200000" + i)
                    .setMemberId((long) i % 6 + 1)
                    .setAmount(RandomUtils.nextFloat(10.0f, 30.0f))
                    .setQuantity(RandomUtils.nextFloat(100.0f, 300.0f))
                    .setSalesAt(Timestamp.valueOf("2020-05-01 09:30:00")));
        }

        int[] createdMembersCount = Member.create(members.toArray(new Member[]{}),
                true, true);
        int[] createdOrderCount = Order.create(orders.toArray(new Order[]{}),
                true, true);

        Assert.assertEquals(createdMembersCount.length, 6);
        Assert.assertEquals(createdOrderCount.length, 100);
    }

    @Test
    public void queryFirstMemberWithOrders() throws SQLException {
        prepareRelationData();

        Relationship[] orderRelation = new Relationship[]{Member.HAS_MANY_ORDERS};
        Member member = Member.queryFirst("id = ?", orderRelation, 3);

        Assert.assertNotNull(member);
        Assert.assertTrue(member.getOrders().size() > 0);
    }

    @Test
    public void queryHasMany() throws SQLException {
        prepareRelationData();

        Relationship[] orderRelation = new Relationship[]{Member.HAS_MANY_ORDERS};
        List<Member> members = Member.query("id > (?)", orderRelation, 1);

        Assert.assertTrue(members.size() > 0);
        Assert.assertTrue(members.get(0).getOrders().size() > 0);

        Member firstMember = members.get(0);
        Order firstOrder = firstMember.getOrders().get(0);

        Assert.assertEquals(firstMember.getId(), firstOrder.getMemberId());
    }

    @Test
    public void queryBelongsTo() throws SQLException {
        prepareRelationData();

        Relationship[] memberRelation = new Relationship[]{Order.BELONGS_TO_MEMBER};
        Order order = Order.queryByPrimaryKey(1L, memberRelation);

        Assert.assertNotNull(order);
        Assert.assertNotNull(order.getMember());
        Assert.assertEquals(order.getMember().getId(), order.getMemberId());
    }
}
