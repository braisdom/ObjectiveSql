package com.github.braisdom.example;

import com.github.braisdom.example.model.Member;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;

@RestController
public class MembersController {

    /**
     * URL: POST http://localhost:8080/members
     * Request payload:
     * {
     *     "no": "00001",
     *     "name": "Braisdom",
     *     "gender": 1,
     *     "mobile": "18900000000",
     *     "otherInfo": "none"
     * }
     * @param rawMember
     * @return
     * @throws SQLException
     */
    @PostMapping("/members")
    public Member create(@RequestBody Map<String, Object> rawMember) throws SQLException {
        Member dirtyMember = Member.newInstanceFrom(rawMember, false);
        return Member.create(dirtyMember, true);
    }
}
