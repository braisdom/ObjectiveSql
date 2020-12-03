package com.github.braisdom.objsql.sample.controllers;

import com.github.braisdom.objsql.sample.ResponseObject;
import com.github.braisdom.objsql.sample.model.Product;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
public class ProductsController {

    @GetMapping("/products/calculate_sl_sply")
    public ResponseObject calculateSlSply(@RequestParam("begin") String begin,
                                          @RequestParam("end") String end) throws SQLException, SQLSyntaxException {
        List<Product> productSLSply = Product.calProductSPLYAndLP(begin, end);
        return ResponseObject.createSuccessResponse(productSLSply);
    }
}
