package org.studysync.studysync.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "study_groups")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudyGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String groupName;

    @Column(length = 255)
    private String studyTopic;

    @Column(length = 255)
    private String studyGoal;
}

