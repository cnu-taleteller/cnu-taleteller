package com.cnu.taleteller.backend.domain.user.Repository;

import com.cnu.taleteller.backend.domain.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberEmail(String memberEmail);

}
