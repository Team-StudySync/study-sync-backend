package org.studysync.studysync.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "attendances")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private StudyGroup group;

    @Column(nullable = false)
    private LocalDate attendance_day;

    private LocalDateTime attendanceTime;

    @Column(nullable = false)
    private Boolean isAttended;
}
