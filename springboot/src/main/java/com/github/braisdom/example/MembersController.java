package com.github.braisdom.example;

import com.github.braisdom.example.model.Member;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
public class MembersController {

    /**
     * URL: POST http://localhost:8080/members
     * Request JSON payload: {"no": "00001", "name": "Braisdom", "gender": 1, "mobile": "18900000000", "otherInfo": "none" }
     *
     * @param rawMember
     * @return
     * @throws SQLException
     */
    @PostMapping("/members")
    public Member create(@RequestBody Map<String, Object> rawMember) throws SQLException {
        Member dirtyMember = Member.newInstanceFrom(rawMember, false);
        return Member.create(dirtyMember, true);
    }

    /**
     * URL: GET http://localhost:8080/members/00001
     *
     * @param memberNo
     * @return
     * @throws SQLException
     */
    @GetMapping("/members/{no}")
    public Member getMember(@PathVariable("no") String memberNo) throws SQLException {
        return Member.queryByNo(memberNo);
    }

    /**
     * URL: GET http://localhost:8080/members/00001
     *
     * @return
     * @throws SQLException
     */
    @GetMapping("/members")
    public List<Member> getMembers() throws SQLException {
        return Member.queryAll();
    }
}
