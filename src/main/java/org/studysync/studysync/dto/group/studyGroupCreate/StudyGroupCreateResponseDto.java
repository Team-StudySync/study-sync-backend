package org.studysync.studysync.dto.group.studyGroupCreate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.studysync.studysync.dto.common.StudyGroupInfo;

@Getter
@AllArgsConstructor
@Builder
public class StudyGroupCreateResponseDto {
   @Schema(description = "관리자 멤버 아이디", example = "1")
   private Long adminId;
   private StudyGroupInfo studyGroup;

   public static StudyGroupCreateResponseDto fromDto(StudyGroupCreateDto dto) {
      return StudyGroupCreateResponseDto.builder()
              .adminId(dto.getAdminId())
              .studyGroup(dto.getStudyGroup())
              .build();
   }
}
