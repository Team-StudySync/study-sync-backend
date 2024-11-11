package org.studysync.studysync.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "study_groups")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StudyGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String uuid;

    private String groupImage;

    @Column(nullable = false, unique = true)
    private String groupName;

    @Column(length = 255)
    private String studyInfo;

    @Column(length = 255)
    private String studyGoal;

    public static StudyGroup of(final String groupName, final String studyInfo, final String studyGoal) {
        return StudyGroup.builder()
                .groupName(groupName)
                .studyInfo(studyInfo)
                .studyGoal(studyGoal)
                .uuid(UUID.randomUUID().toString())
                .build();
    }
}

