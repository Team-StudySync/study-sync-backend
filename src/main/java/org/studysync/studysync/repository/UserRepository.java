package org.studysync.studysync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studysync.studysync.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySnsId(String snsId);
}
