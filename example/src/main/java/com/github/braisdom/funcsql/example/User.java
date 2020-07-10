package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.annotations.FuncSql;
import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.github.braisdom.funcsql.annotations.Table;
import lombok.Data;

import java.util.List;

@Data
@Table("users")
@FuncSql
@PrimaryKey(value = "id", relatedClass = UserProfile.class)
public class User {

    private int id;
    private String name;

    private List<UserProfile> userProfiles;
    private Domain domain;
}
