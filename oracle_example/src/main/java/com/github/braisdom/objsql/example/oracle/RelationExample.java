package com.github.braisdom.objsql.example.oracle;

import com.github.braisdom.objsql.example.oracle.Domains.Member;
import com.github.braisdom.objsql.example.oracle.Domains.Order;
import com.github.braisdom.objsql.relation.Relationship;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RelationExample {

    private static final String[] MEMBER_NAMES = {"Joe", "Juan", "Jack", "Albert", "Jonathan", "Justin", "Terry"};

    private static void prepareRelationData() throws SQLException {
        List<Member> members = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            members.add(new Member()
                    .setId(BigDecimal.valueOf(i))
                    .setNo("Q200000" + i)
                    .setName(MEMBER_NAMES[i])
                    .setGender(0)
                    .setMobile("150000000" + i));
        }

        for (int i = 0; i < 100; i++) {
            orders.add(new Order()
                    .setId(BigDecimal.valueOf(i))
                    .setNo("20200000" + i)
                    .setMemberId(BigDecimal.valueOf(i % 6 + 1))
                    .setAmount(BigDecimal.valueOf(RandomUtils.nextFloat(10.0f, 30.0f)))
                    .setQuantity(BigDecimal.valueOf(RandomUtils.nextFloat(100.0f, 300.0f)))
                    .setSalesAt(Timestamp.valueOf("2020-05-01 09:30:00")));
        }

        int[] createdMembersCount = Member.create(members.toArray(new Member[]{}),
                true);
        int[] createdOrderCount = Order.create(orders.toArray(new Order[]{}),
                true);

        Assert.assertEquals(createdMembersCount.length, 6);
        Assert.assertEquals(createdOrderCount.length, 100);
    }

    private static void queryFirstMemberWithOrders() throws SQLException {
        // The member should associate with orders
        Member member = Member.queryFirst("id = ?",
                new Relationship[]{Member.HAS_MANY_ORDERS}, 67);

        Assert.assertNotNull(member);
        Assert.assertNotNull(member.getOrders());
        Assert.assertTrue(member.getOrders().size() > 0);
    }

    private static void queryManyMembersWithOrders() throws SQLException {
        List<Member> members = Member.query("id > (?)",
                new Relationship[]{Member.HAS_MANY_ORDERS}, 1);

        System.out.println();
    }

    private static void queryOrder() throws SQLException {
        // The order should associate with a member
        Order order = Order.queryByPrimaryKey(BigDecimal.valueOf(10255), Order.BELONGS_TO_MEMBER);

        Assert.assertNotNull(order);
        Assert.assertNotNull(order.getMember());
    }

    public static void run() throws SQLException {
        prepareRelationData();
        queryFirstMemberWithOrders();
        queryManyMembersWithOrders();
        queryOrder();
    }
}
