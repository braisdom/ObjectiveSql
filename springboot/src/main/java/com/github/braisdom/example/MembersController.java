package com.github.braisdom.example;

import com.github.braisdom.example.model.Member;
import com.github.braisdom.example.model.Order;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class MembersController {

    @PostMapping("/members")
    public Member create(@RequestBody RequestObject rawMember) throws SQLException {
        Member dirtyMember = Member.newInstanceFrom(rawMember, false);
        return Member.create(dirtyMember, true);
    }

    @GetMapping("/members/{no}")
    public Member getMember(@PathVariable("no") String memberNo) throws SQLException {
        return Member.queryByNo(memberNo);
    }

    @GetMapping("/members")
    public List<Member> getMembers() throws SQLException {
        return Member.queryAll();
    }

    @GetMapping("/members/{no}/orders")
    public Member getMemberOrders(@PathVariable("no") String no) throws SQLException {
        return Member.queryByNo(no, Member.HAS_MANY_ORDERS, Order.HAS_MANY_ORDER_LINES);
    }
}
