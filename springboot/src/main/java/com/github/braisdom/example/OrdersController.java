package com.github.braisdom.example;

import com.github.braisdom.example.model.Member;
import com.github.braisdom.example.model.Order;
import com.github.braisdom.objsql.relation.Relationship;
import com.sun.tools.corba.se.idl.constExpr.Or;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class OrdersController {

    @PostMapping("/orders/{memberNo}")
    public Order makeOrder(@PathVariable("memberNo") String memberNo,
                           @RequestBody RequestObject rawOrder) throws SQLException {
        Member owner = Member.queryByNo(memberNo);
        return Order.makeOrder(owner, rawOrder);
    }

    @GetMapping("/orders/{memberNo}")
    public List<Order> getOrders(@PathVariable("memberNo") String memberNo) throws SQLException {
        Member owner = Member.queryByNo(memberNo);
        return Order.query("member_id = ?",
                new Relationship[]{ Order.HAS_MANY_ORDER_LINES }, owner.getId());
    }
}
