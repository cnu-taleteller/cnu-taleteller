package com.cnu.taleteller.backend.domain.user.Repository;

import com.cnu.taleteller.backend.domain.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberEmail(String memberEmail);
    Optional<Member> findByMemberPassword(String memberPassword);


    Member findDistinctByMemberEmail(String memberEmail);

    List<Member> findAllByMemberEmail(String memberEmail);
}
