package com.github.braisdom.example;

import com.github.braisdom.example.model.Member;
import com.github.braisdom.example.model.Order;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class OrdersController {

    @PostMapping("/orders/{memberNo}")
    public Order makeOrder(@PathVariable("memberNo") String memberNo,
                           @RequestBody RequestObject rawOrder) throws SQLException {
        Member owner = Member.queryByNo(memberNo);
        return Order.makeOrder(owner, rawOrder);
    }
}
