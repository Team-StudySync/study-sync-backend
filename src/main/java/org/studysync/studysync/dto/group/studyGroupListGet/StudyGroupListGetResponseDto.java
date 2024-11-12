package org.studysync.studysync.dto.group.studyGroupListGet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class StudyGroupListGetResponseDto {
    List<StudyGroupInfo> groups;

    public static StudyGroupListGetResponseDto fromDto(StudyGroupListGetDto dto) {
        return StudyGroupListGetResponseDto.builder()
                .groups(dto.getGroups())
                .build();
    }
}
