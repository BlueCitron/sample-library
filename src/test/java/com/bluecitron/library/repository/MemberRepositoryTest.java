package com.bluecitron.library.repository;

import com.bluecitron.library.entity.Member;
import com.bluecitron.library.entity.MemberGrade;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원_생성() {
        // then
        Member blueCitron = new Member("BlueCitron", "bluecitron@citron.com");

        // when
        memberRepository.save(blueCitron);

        Member member = memberRepository.findById(blueCitron.getId()).get();

        Assertions.assertThat(member.getAccount()).isEqualTo("BlueCitron");
        Assertions.assertThat(member.getEmail()).isEqualTo("bluecitron@citron.com");
        Assertions.assertThat(member.getGrade()).isEqualTo(MemberGrade.NORMAL);
    }



}