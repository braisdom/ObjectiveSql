package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.RollbackCauseException;
import org.apache.commons.lang3.RandomUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.github.braisdom.objsql.example.Domains.createTables;

public class TransactionalExample {

    public static void createNormally() throws SQLException, RollbackCauseException {
        Domains.Member member = new Domains.Member()
                .setNo("200001")
                .setName("Alice")
                .setGender(0)
                .setMobile("15011112222");
        Domains.Order order = new Domains.Order()
                .setNo("202000001")
                .setMemberId(3)
                .setAmount(3.5f)
                .setQuantity(100.3f)
                .setSalesAt(Timestamp.valueOf("2020-05-01 09:30:00"));

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        for(int i = 0; i< 10; i++) {
            executorService.execute(() -> {
                try {
                    final int randomSleepTime = RandomUtils.nextInt(5, 500);
                    Thread.sleep(randomSleepTime);
                    Databases.executeTransactionally(() -> {
                        Domains.Member.create(member, true);
                        if(randomSleepTime % 2 == 0)
                            throw new IllegalStateException("random rollback");
                        Domains.Order.create(order, true);
                        return null;
                    });
                } catch (SQLException e) {
                } catch (RollbackCauseException e) {
                } catch (InterruptedException e) {
                }
            });
        }
    }

    public static void main(String[] args) throws SQLException, RollbackCauseException {
        File file = new File("transactional.db");

        if (file.exists())
            file.delete();

        Databases.installConnectionFactory(new SqliteConnectionFactory(file.getPath()));
        Connection connection = Databases.getConnectionFactory().getConnection();
        createTables(connection);

        createNormally();
    }
}
