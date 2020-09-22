package com.github.braisdom.example;

import com.github.braisdom.example.analysis.ProductSales;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.DynamicModel;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
public class ProductsController {

    @GetMapping(value = "/product/sales_analysis")
    public ResponseObject calProductSales(@RequestParam Map<String, String> rawRequest)
            throws SQLSyntaxException, SQLException {
        RequestObject requestObject = RequestObject.create(rawRequest);

        String beginning = requestObject.getString("beginning");
        String end = requestObject.getString("end");
        String[] productBarcodes = requestObject.getStringArray("productBarcodes");

        ProductSales productSales = new ProductSales()
                .productIn(productBarcodes)
                .salesBetween(beginning, end);

        List<DynamicModel> result = productSales.execute(Databases.getDefaultDataSourceName());
        return ResponseObject.createSuccessResponse(result);
    }
}
