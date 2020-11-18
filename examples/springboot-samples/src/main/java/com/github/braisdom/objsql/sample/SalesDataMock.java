package com.github.braisdom.objsql.sample;

import com.github.braisdom.objsql.ConnectionFactory;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.sample.model.Member;
import com.github.braisdom.objsql.sample.model.Order;
import com.github.braisdom.objsql.sample.model.OrderLine;
import com.github.braisdom.objsql.sample.model.Product;
import org.apache.commons.lang3.RandomUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SalesDataMock {

    private static String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");

    private static final String[] MEMBER_NAMES = {"Joe", "Juan", "Jack", "Albert", "Jonathan", "Justin", "Terry", "Gerald", "Keith", "Samuel",
            "Willie", "Ralph", "Lawrence", "Nicholas", "Roy", "Benjamin", "Bruce", "Brandon", "Adam", "Harry", "Fred", "Wayne", "Billy", "Steve",
            "Louis", "Jeremy", "Aaron", "Randy", "Howard", "Eugene", "Carlos", "Russell", "Bobby", "Victor", "Martin", "Ernest", "Phillip", "Todd",
            "Jesse", "Craig", "Alan", "Shawn", "Clarence", "Sean", "Philip", "Chris", "Johnny", "Earl", "Jimmy", "Antonio", "James", "John", "Robert",
            "Michael", "William", "David", "Richard", "Charles", "Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George",
            "Kenneth", "Steven", "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy", "Jose", "Larry", "Jeffrey",
            "Frank", "Scott", "Eric", "Stephen", "Andrew", "Raymond", "Gregory", "Joshua", "Jerry", "Dennis", "Walter", "Patrick", "Peter", "Harold",
            "Douglas", "Henry", "Carl", "Arthur", "Ryan", "Roger"};

    private static final String[] PRODUCT_NAMES = {
            "face wash", "toner", "firming lotion", "smoothing toner", "moisturizers and creams", "moisturizer",
            "sun screen", "eye gel", "facial mask", "Lip care", "Lip coat", "facial scrub", "bodylotion", "Shower Gel",
            "eye shadow", "mascara", "lip liner", "makeup remover ", "makeup removing lotion", "baby diapers", "milk powder",
            "toothbrush", "toothpaste", "wine", "beer", "Refrigerator", "television", "Microwave Oven", "rice cooker",
            "coffee", "tea", "milk", "drink", "whisky", "tequila", "Liquid soap"
    };

    public void generateData() throws SQLException {
        List<Member> members = generateMembers();
        List<Product> products = generateProducts();

        generateOrdersAndOrderLines(members, products);
    }

    private List<Member> generateMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        for (int i = 1; i < MEMBER_NAMES.length; i++) {
            Member member = new Member();
            members.add(member.setId(Long.valueOf(i))
                    .setNo(String.format("M20200000%s", (i + 1)))
                    .setName(MEMBER_NAMES[i])
                    .setGender(RandomUtils.nextInt(1, 3))
                    .setMobile(getMobile()));
        }
        Member.create(members.toArray(new Member[]{}), false, true);

        return members;
    }

    private List<Product> generateProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        for (int i = 1; i < PRODUCT_NAMES.length; i++) {
            Product product = new Product();
            products.add(product.setId(Long.valueOf(i))
                    .setName(PRODUCT_NAMES[i])
                    .setBarcode(String.format("6901234567%s", (i + 10)))
                    .setCategoryId(RandomUtils.nextInt(1, 10))
                    .setCost(RandomUtils.nextFloat(5.0f, 40.0f))
                    .setSalesPrice(RandomUtils.nextFloat(10.0f, 50.0f)));
        }
        Product.create(products.toArray(new Product[]{}), false, true);
        return products;
    }

    private List<DateTime> prepareDateTime() {
        List<DateTime> dateTimes = new ArrayList<>();

        DateTime lastYearBegin = DateTime.parse("2019-07-01 00:00:00",
                DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:SS"));
        while (lastYearBegin.isBefore(DateTime.parse("2019-11-30 23:59:59",
                DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:SS")))) {
            dateTimes.add(lastYearBegin);
            lastYearBegin = lastYearBegin.plusDays(1);
        }

        DateTime thisYearBegin = DateTime.parse("2020-07-01 00:00:00",
                DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:SS"));
        while (thisYearBegin.isBefore(DateTime.parse("2020-11-30 23:59:59",
                DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:SS")))) {
            dateTimes.add(thisYearBegin);
            thisYearBegin = thisYearBegin.plusDays(1);
        }

        return dateTimes;
    }

    private void generateOrdersAndOrderLines(List<Member> members, List<Product> products) throws SQLException {
        List<Order> orders = new ArrayList<>();
        List<OrderLine> orderLines = new ArrayList<>();
        List<DateTime> dateTimes = prepareDateTime();

        int orderId = 0;
        int orderLineId = 0;

        for(DateTime dateTime : dateTimes) {
            for (int i = 1; i < 20; i++) {
                Order order = new Order();
                Member member = members.get(RandomUtils.nextInt(1, members.size()));

                int orderLineCount = RandomUtils.nextInt(2, 5);
                float totalAmount = 0;
                float totalQuantity = 0;

                order.setId(Long.valueOf(orderId++))
                        .setNo("O0000000" + orderId++);

                for (int t = 1; t < orderLineCount; t++) {
                    Product product = products.get(RandomUtils.nextInt(0, products.size()));
                    int productQuantity = RandomUtils.nextInt(1, 5);
                    float productAmount = productQuantity * product.getSalesPrice();

                    OrderLine orderLine = new OrderLine();
                    orderLine.setId(Long.valueOf(orderLineId++))
                            .setAmount(productAmount)
                            .setQuantity(Float.valueOf(productQuantity))
                            .setProductId(product.getId())
                            .setBarcode(product.getBarcode())
                            .setSalesPrice(product.getSalesPrice())
                            .setMemberId(member.getId())
                            .setOrderId(order.getId())
                            .setOrderNo(order.getNo());

                    totalAmount += productAmount;
                    totalQuantity += productQuantity;

                    orderLines.add(orderLine);
                }

                dateTime = dateTime.withHourOfDay(RandomUtils.nextInt(8, 20))
                        .withMinuteOfHour(RandomUtils.nextInt(1, 60));
                order.setAmount(totalAmount)
                        .setQuantity(totalQuantity)
                        .setMemberId(member.getId())
                        .setSalesAt(Timestamp.valueOf(dateTime.toString("YYYY-MM-dd HH:mm:SS.0")));

                orders.add(order);
            }
        }

        Order.create(orders.toArray(new Order[]{}), true, true);
        OrderLine.create(orderLines.toArray(new OrderLine[]{}), true, true);
    }

    private static String getMobile() {
        int index = getRandomNum(0, telFirst.length - 1);
        String first = telFirst[index];
        String second = String.valueOf(getRandomNum(1, 888) + 10000).substring(1);
        String third = String.valueOf(getRandomNum(1, 9100) + 10000).substring(1);
        return first + second + third;
    }

    public static int getRandomNum(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    public static void main(String[] args) throws SQLException {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mysql://localhost:4406/objective_sql");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("123456");
        DataSource dataSource = dataSourceBuilder.build();

        Databases.installConnectionFactory(new ConnectionFactory() {
            @Override
            public Connection getConnection(String dataSourceName) throws SQLException {
                return dataSource.getConnection();
            }
        });

        Databases.execute("TRUNCATE TABLE members");
        Databases.execute("TRUNCATE TABLE products");
        Databases.execute("TRUNCATE TABLE orders");
        Databases.execute("TRUNCATE TABLE order_lines");

        new SalesDataMock().generateData();
    }
}
