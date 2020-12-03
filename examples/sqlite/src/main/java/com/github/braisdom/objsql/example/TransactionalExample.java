package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.example.domains.Order;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionalExample extends SQLiteExample {

    @Test
    public void createNormally() {
        Order order = new Order()
                .setNo("202000001")
                .setMemberId(3)
                .setAmount(3.5d)
                .setQuantity(100.3d)
                .setSalesAt(Timestamp.valueOf("2020-05-01 09:30:00"));

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        for(int i = 0; i< 1; i++) {
            executorService.execute(() -> {
                try {
                    Order.makeOrder(order, null);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
