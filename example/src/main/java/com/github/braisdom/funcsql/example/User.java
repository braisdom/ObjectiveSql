package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.*;
import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.HasMany;
import com.github.braisdom.funcsql.annotations.HasOne;
import lombok.Data;

import java.util.List;

@Data
@DomainModel
public class User {

    public static final Relation R_USER_PROFILE = new Relation(RelationType.HAS_MANY, User.class,
            UserProfile.class, null, null, null);

    private int id;
    private String name;

    @HasMany
    private List<UserProfile> userProfiles;

    @HasOne
    private Domain domain;

    public static Query<User> createQuery() {
        return new DefaultQuery(User.class);
    }
}
