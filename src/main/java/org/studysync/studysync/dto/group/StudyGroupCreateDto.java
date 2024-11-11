package org.studysync.studysync.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.studysync.studysync.domain.Member;
import org.studysync.studysync.dto.common.StudyGroupInfo;

@Getter
@AllArgsConstructor
@Builder
public class StudyGroupCreateDto {
   private Long adminId;
   private StudyGroupInfo studyGroup;

   public static StudyGroupCreateDto of(Member admin) {
      return StudyGroupCreateDto.builder()
              .adminId(admin.getId())
              .studyGroup(StudyGroupInfo.fromEntity(admin.getGroup()))
              .build();
   }
}
