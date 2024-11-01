package org.studysync.studysync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studysync.studysync.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
