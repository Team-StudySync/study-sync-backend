package org.studysync.studysync.controller.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.studysync.studysync.config.HttpErrorCode;
import org.studysync.studysync.domain.User;
import org.studysync.studysync.dto.group.studyGroupListGet.StudyGroupListGetResponseDto;
import org.studysync.studysync.dto.member.memberListGet.MemberListGetDto;
import org.studysync.studysync.dto.member.memberListGet.MemberListGetResponseDto;
import org.studysync.studysync.service.member.MemberService;
import org.studysync.studysync.swagger.ApiErrorCodeExample;
import org.studysync.studysync.swagger.ApiErrorCodeExamples;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group/{groupId}/member")
@Tag(name = "멤버")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "멤버 리스트 조회", description = "그룹에 속한 멤버들을 조회합니다.")
    @ApiErrorCodeExamples(value = {
            @ApiErrorCodeExample(value = HttpErrorCode.AccessDeniedError),
            @ApiErrorCodeExample(value = HttpErrorCode.NotValidAccessTokenError),
            @ApiErrorCodeExample(value = HttpErrorCode.ExpiredAccessTokenError),
            @ApiErrorCodeExample(value = HttpErrorCode.UserNotFoundError),
            @ApiErrorCodeExample(value = HttpErrorCode.NoSuchStudyGroupError),
            @ApiErrorCodeExample(value = HttpErrorCode.NoSuchStudyGroupMemberError)
    })
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = MemberListGetResponseDto.class)))
    @GetMapping
    public ResponseEntity<MemberListGetResponseDto> getMembers(@PathVariable String groupId, @AuthenticationPrincipal User user) {
        MemberListGetDto dto = memberService.getMembers(groupId, user);
        return new ResponseEntity<>(MemberListGetResponseDto.fromEntity(dto), HttpStatus.OK);
    }

}
