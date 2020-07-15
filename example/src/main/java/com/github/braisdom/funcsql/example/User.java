package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.DefaultQuery;
import com.github.braisdom.funcsql.Query;
import com.github.braisdom.funcsql.Relation;
import com.github.braisdom.funcsql.RelationType;
import com.github.braisdom.funcsql.annotations.*;
import lombok.Data;

import java.util.List;

@Data
@DomainModel
public class User {

    public static final Relation R_USER_PROFILE = new Relation(RelationType.HAS_MANY,
            "userProfiles", User.class, UserProfile.class, "id", "id",
            "user_id", null);

    @Queryable
    @Column("id")
    private int id;
    @Queryable
    private String name;

    @HasMany(primaryKey = "id", foreignKey = "user_id")
    private List<UserProfile> userProfiles;

    @HasOne
    private Domain domain;

    public static Query<User> createQuery() {
        return new DefaultQuery(User.class);
    }
}
