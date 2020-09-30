package com.github.braisdom.example.controllers;

import com.github.braisdom.example.RequestObject;
import com.github.braisdom.example.ResponseObject;
import com.github.braisdom.example.model.Member;
import com.github.braisdom.example.model.Order;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class OrdersController {

    /**
     * The post body is in resources/json/make_order.json
     */
    @PostMapping("/orders/{memberNo}")
    public ResponseObject makeOrder(
            @PathVariable("memberNo") String memberNo,
            @RequestBody RequestObject rawOrder) throws SQLException {
        Member owner = Member.queryByNo(memberNo);

        Order.makeOrder(owner, rawOrder);
        return ResponseObject.createSuccessResponse();
    }
}
