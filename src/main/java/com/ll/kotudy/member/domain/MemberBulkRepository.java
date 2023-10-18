package com.ll.kotudy.member.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAllMember(List<Member> products) {
        String sql = "INSERT INTO members (username, password, role) " +
                "VALUES (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                products,
                products.size(),
                (PreparedStatement ps, Member member) -> {
                    ps.setString(1, member.getUsername());
                    ps.setString(2, member.getPassword());
                    ps.setString(3, member.getRole().toString());
                });
    }
}
