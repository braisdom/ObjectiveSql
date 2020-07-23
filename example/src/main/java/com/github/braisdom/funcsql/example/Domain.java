package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.annotations.DomainModel;
import lombok.Data;

@Data
@DomainModel
public class Domain {

    private int id;
    private String name;
    private int userId;

}
