package org.studysync.studysync.dto.invitation.InvitationInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class InvitationInfoResponseDto {
    @Schema(example = "http://localhost:8080/image/groupImage.png", description = "그룹 이미지")
    private String groupImage;

    @Schema(example = "알고리즘 스터디", description = "그룹 이미지")
    private String groupName;

    @Schema(example = "함께 알고리즘을 공부하기 위한 스터디 그룹입니다.", description = "그룹 개요")
    private String groupInfo;

    public static InvitationInfoResponseDto fromDto(InvitationInfoDto dto) {
        return InvitationInfoResponseDto.builder()
                .groupImage(dto.getGroupImage())
                .groupName(dto.getGroupName())
                .groupInfo(dto.getGroupInfo())
                .build();
    }
}
