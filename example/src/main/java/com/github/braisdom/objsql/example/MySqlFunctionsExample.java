package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import com.github.braisdom.objsql.sql.Select;
import com.github.braisdom.objsql.sql.expression.LiteralExpression;

import static com.github.braisdom.objsql.sql.function.MySQLFunctions.*;

public class MySqlFunctionsExample {

    private static void dateExample1() throws SQLSyntaxException {
        Select select = new Select();

        select.project(dateAdd("2020-09-20", 10));

        System.out.println(select.prettyFormat(DatabaseType.MySQL));
        System.out.println("---------------------------------------------------");
    }

    private static void dateExample2() throws SQLSyntaxException {
        Select select = new Select();

        select.project(dateSub("2020-09-20", 10));

        System.out.println(select.prettyFormat(DatabaseType.MySQL));
        System.out.println("---------------------------------------------------");
    }

    private static void dateExample3() throws SQLSyntaxException {
        Select select = new Select();

        select.project(date("2020-09-20"));

        System.out.println(select.prettyFormat(DatabaseType.MySQL));
        System.out.println("---------------------------------------------------");
    }

    private static void dateDiffExample() throws SQLSyntaxException {
        Select select = new Select();

        select.project(dateDiff("2020-09-20", "2020-09-10").as("diff"));

        System.out.println(select.prettyFormat(DatabaseType.MySQL));
        System.out.println("---------------------------------------------------");
    }

    private static void extractExample() throws SQLSyntaxException {
        Select select = new Select();

        select.project(extractYearMonth(new LiteralExpression("2020-09-20")).as("year"));

        System.out.println(select.prettyFormat(DatabaseType.MySQL));
        System.out.println("---------------------------------------------------");
    }

    public static void main(String[] args) throws SQLSyntaxException {
        dateExample1();
        dateExample2();
        dateExample3();
        dateDiffExample();
        extractExample();
    }
}
