package com.github.braisdom.example;

import com.github.braisdom.example.model.Member;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MembersController {

    @PostMapping("/members")
    public String create(@RequestBody Map<String, Object> rawMember) throws DomainException {
        Member member = Member.createManually(rawMember);
        return "ok";
    }
}
