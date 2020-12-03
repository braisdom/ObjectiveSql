package com.github.braisdom.objsql.sample.controllers;

import com.github.braisdom.objsql.sample.RequestObject;
import com.github.braisdom.objsql.sample.ResponseObject;
import com.github.braisdom.objsql.sample.model.Member;
import com.github.braisdom.objsql.sample.model.Order;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
