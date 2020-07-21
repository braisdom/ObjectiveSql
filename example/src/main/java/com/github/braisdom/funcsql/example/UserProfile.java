package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.Relation;
import com.github.braisdom.funcsql.relation.RelationType;
import com.github.braisdom.funcsql.relation.Relationship;
import lombok.Data;

@Data
@DomainModel
public class UserProfile {

    public static final Relationship RBE_USER_PROFILE = Relationship
            .createRelation(UserProfile.class, "user");

    private int id;
    private String name;
    private int userId;

    @Relation(relationType = RelationType.BELONGS_TO)
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public UserProfile setUser(User user) {
        this.user = user;
        return this;
    }
}
