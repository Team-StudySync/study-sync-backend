package org.studysync.studysync.dto.group.studyGroupListGet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.studysync.studysync.domain.StudyGroup;

@Getter
@AllArgsConstructor
@Builder
public class StudyGroupInfo {
    @Schema(example = "그룹 아이디")
    private String groupId;

    @Schema(example = "그룹 이름")
    private String groupName;

    @Schema(example = "그룹 프로필 이미지")
    private String groupImage;

    public static StudyGroupInfo fromEntity(StudyGroup studyGroup) {
        return StudyGroupInfo.builder()
                .groupId(studyGroup.getUuid())
                .groupName(studyGroup.getGroupName())
                .groupImage(studyGroup.getGroupImage())
                .build();
    }
}
