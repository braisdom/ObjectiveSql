package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.DefaultQuery;
import com.github.braisdom.funcsql.Query;
import com.github.braisdom.funcsql.relation.Relationship;
import com.github.braisdom.funcsql.relation.RelationType;
import com.github.braisdom.funcsql.annotations.*;
import lombok.Data;

import java.util.List;

@Data
@DomainModel(tableName = "users")
public class User {

    public static final Relationship MR_USER_PROFILE = Relationship
            .createRelation(User.class, "userProfiles");

    @PrimaryKey
    private int userId;

    @Queryable
    private String name;

    @Relation(relationType = RelationType.HAS_MANY)
    private List<UserProfile> userProfiles;

    private Domain domain;

    @Transactional
    public static User register(User rawUser) {
        return null;
    }

    public static Query<User> createQuery() {
        return new DefaultQuery(User.class);
    }
}
