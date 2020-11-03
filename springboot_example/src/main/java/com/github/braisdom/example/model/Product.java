package com.github.braisdom.example.model;

import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.Queryable;
import com.github.braisdom.objsql.sql.Dataset;

import java.util.Date;
import java.util.List;

@DomainModel
public class Product {
    @Queryable
    private String barcode;
    @Queryable
    private String name;
    private Integer categoryId;
    private Float salesPrice;
    private Float cost;

    /**
     * Calculate Same Period Last Year of products sales
     *
     * @return
     */
    public static List<Product> calSPLY(Date begin, Date end, String[] barcodes) {
        return null;
    }

    private static Dataset getPeriodProductsSales(Date begin, Date end) {
        return null;
    }
}
