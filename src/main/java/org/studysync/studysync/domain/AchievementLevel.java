package org.studysync.studysync.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity(name = "achievement_levels")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AchievementLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private StudyGroup group;

    @Column(nullable = false)
    private LocalDate achievement_day;

    @Column(nullable = false)
    private Double achievement;
}
