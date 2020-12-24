package com.github.braisdom.objsql.benchmark;

import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.PrimaryKey;
import com.github.braisdom.objsql.annotations.Transient;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "user")
@Table(name = "user")
@DomainModel(tableName = "user")
public class User implements Serializable {

    @Id
    @PrimaryKey
    private Integer id;
    private String name;
    private int age;

    @Transient
    @javax.persistence.Transient
    private Map<String, Object> rawAttributes = new HashMap<>();

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

    public Map<String, Object> getRawAttributes() {
        return rawAttributes;
    }
}
