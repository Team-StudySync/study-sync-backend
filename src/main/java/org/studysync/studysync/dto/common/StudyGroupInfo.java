package org.studysync.studysync.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.studysync.studysync.domain.StudyGroup;

@Getter
@AllArgsConstructor
@Builder
public class StudyGroupInfo {
    @Schema(description = "그룹 아이디", example = "219f4fa2-2155-423f-9443-55514963efs3")
    private String uuid;

    @Schema(description = "그룹 이름", example = "알고리즘 스터디")
    private String groupName;

    @Schema(description = "그룹 개요", example = "함께 알고리즘을 공부하기 위한 스터디 그룹입니다.")
    private String studyInfo;

    @Schema(description = "그룹 목표", example = "하루에 하나 이상의 알고리즘 문제 풀이를 제출해야 합니다.")
    private String studyGoal;


    public static StudyGroupInfo fromEntity(StudyGroup studyGroup) {
        return StudyGroupInfo.builder()
                .uuid(studyGroup.getUuid())
                .groupName(studyGroup.getGroupName())
                .studyInfo(studyGroup.getStudyInfo())
                .studyGoal(studyGroup.getStudyGoal())
                .build();
    }
}
