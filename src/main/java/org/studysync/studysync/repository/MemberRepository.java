package org.studysync.studysync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studysync.studysync.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
