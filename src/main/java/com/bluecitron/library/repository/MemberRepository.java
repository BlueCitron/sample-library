package com.bluecitron.library.repository;

import com.bluecitron.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByAccount(String account);
}
