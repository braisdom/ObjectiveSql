package com.github.braisdom.objsql.benchmark;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class Hibernate implements ORMFramework {

    public static final String FRAMEWORK_NAME = "hibernate";

    private final HikariDataSource dataSource;
    private final SessionFactory sessionFactory;

    public Hibernate(HikariDataSource dataSource) {
        this.dataSource = dataSource;
        sessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();
    }

    @Override
    public void initialize() throws Exception {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User user = new User();
        user.setId(1);
        user.setName("ash");
        user.setAge(25);
        session.save( user );
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void update() {

    }

    @Override
    public User query() throws Exception {
        Session session = sessionFactory.openSession();
        Query<User> query = session.createQuery("from com.github.braisdom.objsql.benchmark.User " +
                "user where user.id = 1");
        User user = query.getSingleResult();
        session.close();
        return user;
    }

    @Override
    public void teardown() {
        dataSource.close();
        sessionFactory.close();
    }
}
