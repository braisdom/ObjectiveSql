package com.github.braisdom.objsql.benchmark;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import joptsimple.internal.Strings;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class BenchmarkRunner {

    private static String[] createTableStatement = new String[] {
            "DROP TABLE IF EXISTS user;",
            "CREATE TABLE user",
            "(",
            "    id INTEGER,",
            "    name VARCHAR(25),",
            "    age INTEGER,",
            "    PRIMARY KEY (id)",
            ");"
    };

    @Param({MyBatis.FRAMEWORK_NAME, Jdbc.FRAMEWORK_NAME, ObjectiveSQL.FRAMEWORK_NAME})
    public String framework;
    private ORMFramework ormFramework;

    @Setup(Level.Trial)
    public void setup(BenchmarkParams params) throws Exception {
        HikariDataSource dataSource = createDataSource();
        createTable(dataSource);

        ormFramework = ORMFramework.Factory.createORMFramework(framework, dataSource);
        ormFramework.initialize();
    }

    @TearDown(Level.Trial)
    public void teardown() throws SQLException {
        ormFramework.teardown();
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.INLINE)
    public User query() throws Exception {
        return ormFramework.query();
    }

    private HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.hsqldb.jdbcDriver");
        config.setJdbcUrl("jdbc:hsqldb:mem:test");
        config.setUsername("sa");
        config.setPassword("");
        config.setMinimumIdle(16);
        config.setMaximumPoolSize(32);
        config.setConnectionTimeout(8000);
        config.setAutoCommit(true);
        return new HikariDataSource(config);
    }

    private void createTable(DataSource dataSource) {
        Connection conn = null;
        Statement stat = null;
        try {
            conn = createDataSource().getConnection();
            stat = conn.createStatement();
            stat.execute(Strings.join(createTableStatement, " "));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stat != null) {
                    stat.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(BenchmarkRunner.class.getSimpleName())
                .forks(1)
                .warmupIterations(10)
                .measurementIterations(10)
                .build();

        new Runner(opt).run();
    }

}
