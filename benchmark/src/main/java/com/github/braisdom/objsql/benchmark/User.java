package com.github.braisdom.objsql.benchmark;

import com.github.braisdom.objsql.annotations.DomainModel;

@DomainModel(primaryClass = Integer.class, tableName = "user")
public class User {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
