package com.github.braisdom.objsql.benchmark;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;

public class MyBatis implements ORMFramework {

    public static final String FRAMEWORK_NAME = "mybatis";

    private final SqlSessionFactory sqlSessionFactory;
    private final HikariDataSource dataSource;

    public MyBatis(HikariDataSource dataSource) {
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(UserMapper.class);

        this.dataSource = dataSource;
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    @Override
    public void initialize() throws Exception {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            UserMapper mapper = session.getMapper(UserMapper.class);
            mapper.addUser(1, "ash", 25);
            session.commit();
            return;
        } finally {
            session.close();
        }
    }

    @Override
    public void update() {

    }

    @Override
    public User query() throws Exception {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            UserMapper mapper = session.getMapper(UserMapper.class);
            User user = mapper.getUserById(1);
            if(user == null) {
                throw new NullPointerException("The user cannot be null");
            }

            return user;
        } finally {
            session.close();
        }
    }

    @Override
    public void teardown() {
        dataSource.close();
    }

    public interface UserMapper {

        @Insert("insert into user(id, name, age) values(#{id}, #{name}, #{age})")
        void addUser(@Param("id") int id, @Param("name") String name, @Param("age") int age) throws Exception;

        @Select("select id, name, age from user where id = #{id}")
        User getUserById(int id) throws Exception;

        @Update("update user set age = #{age} where id = #{id}")
        void updateUser(@Param("id") int id, @Param("age") int age) throws Exception;

    }
}
