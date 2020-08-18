package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.Database;
import com.github.braisdom.funcsql.relation.Relationship;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RelationExample {

//    private static final String[] MEMBER_NAMES = {"Joe", "Juan", "Jack", "Albert", "Jonathan", "Justin", "Terry"};
//
//    private static void prepareRelationData() throws SQLException {
//        List<Domains.Member> members = new ArrayList<>();
//        List<Domains.Order> orders = new ArrayList<>();
//
//        for (int i = 1; i <= 6; i++) {
//            members.add(new Domains.Member()
//                    .setId(i)
//                    .setNo("Q200000" + i)
//                    .setName(MEMBER_NAMES[i])
//                    .setGender(0)
//                    .setMobile("150000000" + i));
//        }
//
//        for (int i = 0; i < 100; i++) {
//            orders.add(new Domains.Order()
//                    .setNo("20200000" + i)
//                    .setMemberId(i % 6 + 1)
//                    .setAmount(RandomUtils.nextFloat(10.0f, 30.0f))
//                    .setQuantity(RandomUtils.nextFloat(100.0f, 300.0f))
//                    .setSalesAt(Timestamp.valueOf("2020-05-01 09:30:00")));
//        }
//
//        int[] createdMembersCount = Domains.Member.create(members.toArray(new Domains.Member[]{}), true);
//        int[] createdOrderCount = Domains.Order.create(orders.toArray(new Domains.Order[]{}), true);
//
//        Assert.assertEquals(createdMembersCount.length, 6);
//        Assert.assertEquals(createdOrderCount.length, 100);
//    }
//
//    private static void queryFirstMemberWithOrders() throws SQLException {
//        Domains.Member member = Domains.Member.queryFirst("id = ?", new Relationship[]{Domains.Member.HAS_MANY_ORDERS}, 3);
//
//        Assert.assertNotNull(member);
//        Assert.assertTrue(member.getOrders().size() > 0);
//        System.out.println();
//    }
//
//    private static void queryManyMembersWithOrders() throws SQLException {
//        List<Domains.Member> members = Domains.Member.query("id > (?)",
//                new Relationship[]{Domains.Member.HAS_MANY_ORDERS}, 1);
//
//        System.out.println();
//    }
//
//    public static void main(String args[]) throws SQLException {
//        File file = new File("relation_example.db");
//
//        if (file.exists())
//            file.delete();
//
//        Database.installConnectionFactory(new SqliteConnectionFactory(file.getPath()));
//        Connection connection = Database.getConnectionFactory().getConnection();
//        Domains.createTables(connection);
//
//        prepareRelationData();
//        queryFirstMemberWithOrders();
//        queryManyMembersWithOrders();
//    }
}
