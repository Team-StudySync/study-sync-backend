package org.studysync.studysync.dto.invitation.InvitationInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.studysync.studysync.domain.StudyGroup;

@Getter
@AllArgsConstructor
@Builder
public class InvitationInfoDto {
    private String groupImage;
    private String groupName;
    private String groupInfo;

    public static InvitationInfoDto fromEntity(StudyGroup group) {
        return InvitationInfoDto.builder()
                .groupImage(group.getGroupImage())
                .groupName(group.getGroupName())
                .groupInfo(group.getStudyInfo())
                .build();
    }
}
