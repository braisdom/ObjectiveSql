package com.github.braisdom.example.model;

import com.github.braisdom.example.DomainException;
import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.Queryable;

import java.sql.SQLException;
import java.util.Map;

@DomainModel
public class Member {
    private String no;
    @Queryable
    private String name;
    private Integer gender;
    private String mobile;
    private String otherInfo;

    public static Member createManually(Map<String, Object> rawMember) throws DomainException {
        try {
            Member member = Member.newInstanceFrom(rawMember, false);
            member.save(true);
            return member;
        }catch (SQLException ex) {
            throw new DomainException(ex.getMessage(), ex);
        }
    }
}
