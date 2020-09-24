package com.github.braisdom.example;

import com.github.braisdom.example.analysis.ProductSales;
import com.github.braisdom.example.analysis.SalesCompareTimeRelated;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.DynamicModel;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
public class ProductsController {

    /**
     * Get parameters:
     * http://host:port//product/sales_analysis?&beginning=2020-01-01 00:00:00
     *      &end=2020-02-01 00:00:00&productBarcodes=p0001,p0002,p0003
     */
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

    @GetMapping(value = "/sales_compare_time_related")
    public ResponseObject claSalesCompare(@RequestBody Map<String, String> rawRequest) throws SQLSyntaxException, SQLException {
        RequestObject requestObject = RequestObject.create(rawRequest);

        String beginTime = requestObject.getString("beginTime");
        String endTime = requestObject.getString("endTime");
        String timeDimension = requestObject.getString("timeDimension");

        SalesCompareTimeRelated salesCompareTimeRelated =
                new SalesCompareTimeRelated()
                        .timeBetween(beginTime, endTime)
                        .timeDimension(timeDimension);
        List<DynamicModel> result = salesCompareTimeRelated.execute(Databases.getDefaultDataSourceName());
        return ResponseObject.createSuccessResponse(result);
    }
}
