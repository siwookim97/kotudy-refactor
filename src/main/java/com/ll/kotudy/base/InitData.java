package com.ll.kotudy.base;

import com.ll.kotudy.member.domain.Member;
import com.ll.kotudy.member.domain.MemberBulkRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Profile("dev")
public class InitData {

    @Bean
    CommandLineRunner initMemberData(MemberBulkRepository memberBulkRepository) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                List<Member> memberList = new ArrayList<>() {
                    {
                        for (int i = 0 ; i < 3000000; i++) {
                            Member createdMember =Member.builder()
                                    .username(UUID.randomUUID().toString())
                                    .password(UUID.randomUUID().toString())
                                    .build();
                            this.add(createdMember);
                        }
                    }
                };
                memberBulkRepository.saveAllMember(memberList);
            }
        };
    }
}