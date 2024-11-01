package org.studysync.studysync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studysync.studysync.domain.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
