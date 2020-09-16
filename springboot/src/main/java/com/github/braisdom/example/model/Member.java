package com.github.braisdom.example.model;

import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.Queryable;

@DomainModel
public class Member {
    private String no;
    @Queryable
    private String name;
    private Integer gender;
    private String mobile;
    private String otherInfo;

}
