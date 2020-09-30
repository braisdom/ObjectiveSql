package com.github.braisdom.example;

import com.github.braisdom.example.model.Member;
import com.github.braisdom.example.model.Order;
import com.github.braisdom.example.model.OrderLine;
import com.github.braisdom.example.model.Product;
import com.github.braisdom.objsql.ConnectionFactory;
import com.github.braisdom.objsql.Databases;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mock {

    private static final String[] MEMBER_NAMES = {"Joe", "Juan", "Jack", "Albert", "Jonathan", "Justin", "Terry", "Gerald", "Keith", "Samuel",
            "Willie", "Ralph", "Lawrence", "Nicholas", "Roy", "Benjamin", "Bruce", "Brandon", "Adam", "Harry", "Fred", "Wayne", "Billy", "Steve",
            "Louis", "Jeremy", "Aaron", "Randy", "Howard", "Eugene", "Carlos", "Russell", "Bobby", "Victor", "Martin", "Ernest", "Phillip", "Todd",
            "Jesse", "Craig", "Alan", "Shawn", "Clarence", "Sean", "Philip", "Chris", "Johnny", "Earl", "Jimmy", "Antonio", "James", "John", "Robert",
            "Michael", "William", "David", "Richard", "Charles", "Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George",
            "Kenneth", "Steven", "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy", "Jose", "Larry", "Jeffrey",
            "Frank", "Scott", "Eric", "Stephen", "Andrew", "Raymond", "Gregory", "Joshua", "Jerry", "Dennis", "Walter", "Patrick", "Peter", "Harold",
            "Douglas", "Henry", "Carl", "Arthur", "Ryan", "Roger"};

    private static final String[] PRODUCT_NAMES = {
            "face wash", "toner", "firming lotion", "smoothing toner", "moisturizers and creams", "", "moisturizer",
            "sun screen", "eye gel", "facial mask", "Lip care", "Lip coat", "facial scrub", "bodylotion", "Shower Gel",
            "eye shadow", "mascara", "lip liner", "makeup remover ", "makeup removing lotion", "baby diapers", "milk powder",
            "toothbrush", "toothpaste", "wine", "beer", "Refrigerator", "television", "Microwave Oven", "rice cooker",
            "coffee", "tea", "milk", "drink", "whisky", "tequila", "Liquid soap"
    };

    private static final String[] SALES_TIMES = {
            "2020-09-01 13:41:01", "2020-09-01 09:23:34", "2020-09-02 10:15:59", "2020-09-02 15:54:12",
            "2020-09-03 08:41:03", "2020-09-03 16:33:09", "2020-09-04 09:13:41", "2020-09-04 12:01:23",
            "2019-09-01 13:41:01", "2019-09-01 09:23:34", "2019-09-02 10:15:59", "2019-09-02 15:54:12",
            "2019-09-03 08:41:03", "2019-09-03 16:33:09", "2019-09-04 09:13:41", "2019-09-04 12:01:23",
    };

    public void generateData() throws SQLException {
        generateMembers();
        generateProducts();
        generateOrdersAndOrderLines();
    }

    private void generateMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < MEMBER_NAMES.length; i++) {
            Member member = new Member();
            members.add(member.setNo(String.format("M20200000%s", (i + 1)))
                    .setName(MEMBER_NAMES[i])
                    .setGender(RandomUtils.nextInt(1, 3))
                    .setMobile(getMobile()));
        }
        int[] createdMembersCount = Member.create(members.toArray(new Member[]{}), false);
    }

    private void generateProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < PRODUCT_NAMES.length; i++) {
            Product product = new Product();
            products.add(product.setName(PRODUCT_NAMES[i])
                    .setBarcode(String.format("P20200000%s", (i + 1)))
                    .setCategoryId(RandomUtils.nextInt(1, 10))
                    .setCost(RandomUtils.nextDouble(5.0f, 40.0f))
                    .setSalesPrice(RandomUtils.nextDouble(10.0f, 50.0f)));
        }
        int[] createdProductsCount = Product.create(products.toArray(new Product[]{}), false);
    }

    private void generateOrdersAndOrderLines() throws SQLException {
        List<OrderLine> orderLines = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Order order = new Order();
            OrderLine orderLine = new OrderLine();

            long memberId = RandomUtils.nextInt(1, MEMBER_NAMES.length + 1);
            String orderNo = String.format("O20200000%s", (i + 1));
            Timestamp salesAt = Timestamp.valueOf(SALES_TIMES[RandomUtils.nextInt(0, SALES_TIMES.length)]);
            order.setNo(orderNo).setMemberId(memberId).setSalesAt(salesAt);
            order = Order.create(order, false);

            float amount = 0f;
            float quantitySum = 0f;
            for (int productKinds = 0; productKinds < RandomUtils.nextInt(1, 5); productKinds++) {
                double quantity = RandomUtils.nextDouble(1.0f, 5.0f);
                String productName = PRODUCT_NAMES[RandomUtils.nextInt(1, PRODUCT_NAMES.length)];
                Product product = Product.queryByName(productName);
                amount += product.getSalesPrice() * quantity;
                quantitySum += quantity;
                orderLine.setOrderId(order.getId())
                        .setOrderNo(orderNo)
                        .setBarcode(product.getBarcode())
                        .setSalesPrice(product.getSalesPrice())
                        .setAmount(product.getSalesPrice() * quantity)
                        .setQuantity(quantity)
                        .setMemberId(memberId)
                        .setSalesPrice(RandomUtils.nextDouble(10.0f, 50.0f))
                        .setProductId(product.getId());
                orderLines.add(orderLine);
            }

            order.setAmount(amount);
            order.setQuantity(quantitySum);
            order.save(false);
        }
        int[] createOrderLinesCount = OrderLine.create(orderLines.toArray(new OrderLine[]{}), false, false);
    }


    private String getMobile() {
        while (true) {
            String phone = "1";
            Random random = new Random();
            int nextInt = random.nextInt(3);

            if (nextInt == 0) {
                phone = phone + "3" + randomNumber();
            } else if (nextInt == 1) {
                phone = phone + "5" + randomNumber();
            } else {
                phone = phone + "8" + randomNumber();
            }
            Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
            Matcher matcher = pattern.matcher(phone);
            if (matcher.matches())
                return phone;
        }
    }

    private String randomNumber() {
        Random random = new Random();
        int nextInt = random.nextInt(900000000) + 100000000;
        int abs = Math.abs(nextInt);
        return String.valueOf(abs);
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
        new Mock().generateData();
    }
}
