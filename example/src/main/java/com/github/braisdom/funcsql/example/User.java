package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.DefaultQuery;
import com.github.braisdom.funcsql.Query;
import com.github.braisdom.funcsql.Relationship;
import com.github.braisdom.funcsql.RelationType;
import com.github.braisdom.funcsql.annotations.*;
import lombok.Data;

import java.util.List;

@Data
@DomainModel(tableName = "users")
public class User {

    public static final Relationship R_USER_PROFILE = Relationship.createRelation(User.class, null);

    @Queryable
    @Column("id")
    private int id;

    @Queryable
    private String name;

    @Relation(relationType = RelationType.HAS_MANY)
    private List<UserProfile> userProfiles;

    private Domain domain;

    public static Query<User> createQuery() {
        return new DefaultQuery(User.class);
    }
}
