package org.studysync.studysync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studysync.studysync.domain.PostStatus;

public interface PostStatusRepository extends JpaRepository<PostStatus, Long> {
}
