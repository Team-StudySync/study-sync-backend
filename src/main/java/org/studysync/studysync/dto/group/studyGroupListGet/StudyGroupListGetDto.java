package org.studysync.studysync.dto.group.studyGroupListGet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.studysync.studysync.domain.StudyGroup;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class StudyGroupListGetDto {
    List<StudyGroupInfo> groups;

    public static StudyGroupListGetDto fromEntity(List<StudyGroup> groups) {
        return StudyGroupListGetDto.builder()
                .groups(groups.stream().map(StudyGroupInfo::fromEntity).toList())
                .build();
    }
}
