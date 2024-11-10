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
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private StudyGroup group;

    @Column(nullable = false)
    private LocalDate achievementDay;

    @Column(nullable = false)
    private Double achievement;
}
