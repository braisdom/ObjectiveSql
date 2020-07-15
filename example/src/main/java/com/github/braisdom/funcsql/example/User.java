package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.HasMany;
import com.github.braisdom.funcsql.annotations.HasOne;
import com.github.braisdom.funcsql.annotations.Queryable;
import lombok.Data;

import java.util.List;

@Data
@DomainModel
public class User {


    @Queryable private int id;
    @Queryable private String name;

    @HasMany
    private List<UserProfile> userProfiles;

    @HasOne
    private Domain domain;

//    public static Query<User> createQuery() {
//        return new DefaultQuery(User.class);
//    }
}
