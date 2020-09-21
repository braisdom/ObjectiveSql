package com.github.braisdom.example.model;

import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.Queryable;

@DomainModel
public class Product {
    @Queryable
    private String barcode;

    @Queryable
    private String name;

    private Integer categoryId;

    private Double salesPrice;

    private Double cost;
}
