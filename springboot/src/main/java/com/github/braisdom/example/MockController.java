package com.github.braisdom.example;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class MockController {

    @GetMapping("/mock")
    public ResponseObject generateData() throws SQLException {
        Mock mock = new Mock();
        mock.generateData();
        return ResponseObject.createSuccessResponse();
    }
}
