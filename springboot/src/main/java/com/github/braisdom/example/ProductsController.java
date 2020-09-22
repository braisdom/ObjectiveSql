package com.github.braisdom.example;

import com.github.braisdom.example.analysis.ProductSales;
import com.github.braisdom.example.analysis.StatisticsObject;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.DynamicModel;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
public class ProductsController {

    @GetMapping(value = "/product/sales_analysis")
    public ResponseObject analysisProductSales(@RequestParam Map<String, String> rawRequestObject)
            throws SQLSyntaxException, SQLException {
        RequestObject requestObject = RequestObject.create(rawRequestObject);
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
