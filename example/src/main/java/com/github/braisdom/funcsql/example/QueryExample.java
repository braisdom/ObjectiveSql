package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.HelloImmutable;

public class QueryExample {

//    private static final String[] MEMBER_NAMES = {"Joe","Juan","Jack","Albert","Jonathan","Justin","Terry","Gerald","Keith","Samuel",
//            "Willie","Ralph","Lawrence","Nicholas","Roy","Benjamin","Bruce","Brandon","Adam","Harry","Fred","Wayne","Billy","Steve",
//            "Louis","Jeremy","Aaron","Randy","Howard","Eugene","Carlos","Russell","Bobby","Victor","Martin","Ernest","Phillip","Todd",
//            "Jesse","Craig","Alan","Shawn","Clarence","Sean","Philip","Chris","Johnny","Earl","Jimmy","Antonio","James","John","Robert",
//            "Michael","William","David","Richard","Charles","Joseph","Thomas","Christopher","Daniel","Paul","Mark","Donald","George",
//            "Kenneth","Steven","Edward","Brian","Ronald","Anthony","Kevin","Jason","Matthew","Gary","Timothy","Jose","Larry","Jeffrey",
//            "Frank","Scott","Eric","Stephen","Andrew","Raymond","Gregory","Joshua","Jerry","Dennis","Walter","Patrick","Peter","Harold",
//            "Douglas","Henry","Carl","Arthur","Ryan","Roger"};
//
//    private static void prepareQueryData() throws SQLException {
//        List<Domains.Member> members = new ArrayList<>();
//        List<Domains.Order> orders = new ArrayList<>();
//
//        for (int i = 0; i < 100; i++) {
//            members.add(new Domains.Member()
//                    .setNo("Q200000" + i)
//                    .setName(MEMBER_NAMES[i])
//                    .setGender(0)
//                    .setMobile("150000000" + i));
//        }
//
//        for (int i = 0; i < 100; i++) {
//            orders.add(new Domains.Order()
//                    .setNo("20200000" + i)
//                    .setMemberId(i)
//                    .setAmount(RandomUtils.nextFloat(10.0f, 30.0f))
//                    .setQuantity(RandomUtils.nextFloat(100.0f, 300.0f))
//                    .setSalesAt(Timestamp.valueOf("2020-05-01 09:30:00")));
//        }
//
//        int[] createdMembersCount = Domains.Member.create(members.toArray(new Domains.Member[]{}), true);
//        int[] createdOrderCount = Domains.Order.create(orders.toArray(new Domains.Order[]{}), true);
//        Assert.assertEquals(createdMembersCount.length, 100);
//        Assert.assertEquals(createdOrderCount.length, 100);
//    }
//
//    private static void countMember() throws SQLException {
//        int count = Domains.Member.count("id > ?", 10);
//
//        Assert.assertEquals(count, 90);
//    }
//
//    private static void queryByName() throws SQLException {
//        List<Domains.Member> member = Domains.Member.queryByName("Ralph");
//        Assert.assertEquals(member.get(0).getName(), "Ralph");
//        Assert.assertEquals(member.get(0).getId(), Integer.valueOf(12));
//    }
//
//    private static void rawQuery() throws SQLException {
//        List<Domains.Member> members = Domains.Member.queryBySql("SELECT id, name FROM members WHERE id < ?", 10);
//        List<Domains.Member> members2 = Domains.Member.queryBySql("SELECT * FROM members WHERE name = ?", "Jonathan");
//        List<Domains.Member> members3 = Domains.Member.queryBySql("SELECT name AS _name FROM members WHERE name = ?", "Jonathan");
//
//        Assert.assertEquals(members.size(), 9);
//        Assert.assertEquals(members2.size(), 1);
//        Assert.assertTrue(members3.size() > 0);
//        Assert.assertEquals(members3.get(0).getRawAttribute("_name"), "Jonathan");
//    }
//
//    private static void queryFirst() throws SQLException {
//        Domains.Member member = Domains.Member.queryFirst("id = ?", 11);
//
//        Assert.assertNotNull(member);
//        Assert.assertEquals(member.getName(), "Willie");
//    }
//
//    private static void queryByPredicate() throws SQLException {
//        List<Domains.Member> members = Domains.Member.query("id > ?", 8);
//
//        Assert.assertNotNull(members);
//        Assert.assertEquals(members.size(), 92);
//    }
//
//    private static void queryOrders() throws SQLException {
//        List<Domains.Order> orders = Domains.Order.query("");
//
//        Assert.assertNotNull(orders);
//        Assert.assertTrue(orders.size() > 0);
//    }
//
//    public static void main(String[] args) throws SQLException {
//        File file = new File("query_example.db");
//
//        if (file.exists())
//            file.delete();
//
//        Database.installConnectionFactory(new SqliteConnectionFactory(file.getPath()));
//        Connection connection = Database.getConnectionFactory().getConnection();
//        Domains.createTables(connection);
//
//        prepareQueryData();
//        countMember();
//        rawQuery();
//        queryByName();
//        queryFirst();
//        queryByPredicate();
//        queryOrders();
//    }
    public static void main(String[] args) {
        HelloImmutable immutable = new HelloImmutable();
    }
}
