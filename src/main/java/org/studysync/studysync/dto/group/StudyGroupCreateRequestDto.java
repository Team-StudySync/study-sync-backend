package org.studysync.studysync.dto.group;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyGroupCreateRequestDto {
    @Schema(description = "그룹 이름", example = "알고리즘 스터디")
    @Size(min = 1, max = 20, message = "그룹 이름은 1~20글자 사이어야 합니다.")
    @NotBlank(message = "그룹 이름은 필수입니다.")
    private String groupName;

    @Schema(description = "그룹 개요", example = "함께 알고리즘을 공부하기 위한 스터디 그룹입니다.")
    @Size(min = 1, max = 100, message = "그룹 주제는 1~100글자 사이어야 합니다.")
    @NotBlank(message = "그룹 주제는 필수입니다.")
    private String studyInfo;

    @Schema(description = "그룹 목표", example = "하루에 하나 이상의 알고리즘 문제 풀이를 제출해야 합니다.")
    @Size(min = 1, max = 100, message = "그룹 주제는 1~500글자 사이어야 합니다.")
    @NotBlank(message = "그룹 목표는 필수입니다.")
    private String studyGoal;
}
