package org.studysync.studysync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studysync.studysync.domain.Member;
import org.studysync.studysync.domain.StudyGroup;
import org.studysync.studysync.domain.User;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByGroupAndUser(StudyGroup group, User user);
    List<Member> findByUser(User user);
    List<Member> findByGroup(StudyGroup group);
}
