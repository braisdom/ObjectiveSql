package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.example.Domains.Member;
import com.github.braisdom.objsql.relation.Relationship;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.github.braisdom.objsql.example.Domains.Order;

public class RelationExample {

    private static final String[] MEMBER_NAMES = {"Joe", "Juan", "Jack", "Albert", "Jonathan", "Justin", "Terry"};

    private static void prepareRelationData() throws SQLException {
        List<Member> members = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            members.add(new Member()
                    .setId(Long.valueOf(i))
                    .setNo("Q200000" + i)
                    .setName(MEMBER_NAMES[i])
                    .setGender(0)
                    .setMobile("150000000" + i));
        }

        for (int i = 0; i < 100; i++) {
            orders.add(new Order()
                    .setId(Long.valueOf(i))
                    .setNo("20200000" + i)
                    .setMemberId(Long.valueOf(i % 6 + 1))
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

    private static void queryFirstMemberWithOrders() throws SQLException {
        Member member = Member.queryFirst("id = ?",
                new Relationship[]{Member.HAS_MANY_ORDERS}, 3);

        Assert.assertNotNull(member);
        Assert.assertTrue(member.getOrders().size() > 0);
    }

    private static void queryManyMembersWithOrders() throws SQLException {
        List<Member> members = Member.query("id > (?)",
                new Relationship[]{Member.HAS_MANY_ORDERS}, 1);

        System.out.println();
    }

    private static void queryOrder() throws SQLException {
        Order order = Order.queryByPrimaryKey(1L, Order.BELONGS_TO_MEMBER);

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
