package org.studysync.studysync.controller.group;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.studysync.studysync.config.HttpErrorCode;
import org.studysync.studysync.domain.User;
import org.studysync.studysync.dto.group.StudyGroupCreateDto;
import org.studysync.studysync.dto.group.StudyGroupCreateRequestDto;
import org.studysync.studysync.dto.group.StudyGroupCreateResponseDto;
import org.studysync.studysync.service.group.StudyGroupService;
import org.studysync.studysync.swagger.ApiErrorCodeExample;
import org.studysync.studysync.swagger.ApiErrorCodeExamples;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
@Tag(name = "그룹")
public class StudyGroupController {
    private final StudyGroupService studyGroupService;

    @Operation(summary = "그룹 생성", description = "새로운 그룹을 생성합니다.")
    @ApiErrorCodeExamples(value = {
            @ApiErrorCodeExample(value = HttpErrorCode.AccessDeniedError),
            @ApiErrorCodeExample(value = HttpErrorCode.NotValidAccessTokenError),
            @ApiErrorCodeExample(value = HttpErrorCode.ExpiredAccessTokenError),
            @ApiErrorCodeExample(value = HttpErrorCode.UserNotFoundError, description = "그룹 생성을 요청한 유저의 정보가 서버에 존재하지 않습니다.")
    })
    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = StudyGroupCreateResponseDto.class)))
    @PostMapping
    public ResponseEntity<StudyGroupCreateResponseDto> createStudyGroup(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody StudyGroupCreateRequestDto requestDto) {
        StudyGroupCreateDto dto = studyGroupService.createStudyGroup(user, requestDto);
        return new ResponseEntity<>(StudyGroupCreateResponseDto.fromDto(dto), HttpStatus.CREATED);
    }
}