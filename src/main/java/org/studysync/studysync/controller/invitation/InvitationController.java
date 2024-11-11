package org.studysync.studysync.controller.invitation;

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
import org.studysync.studysync.dto.invitation.Invitation.InvitationDto;
import org.studysync.studysync.dto.invitation.Invitation.InvitationResponseDto;
import org.studysync.studysync.dto.invitation.InvitationAccept.InvitationAcceptDto;
import org.studysync.studysync.dto.invitation.InvitationAccept.InvitationAcceptResponseDto;
import org.studysync.studysync.dto.invitation.InvitationInfo.InvitationInfoDto;
import org.studysync.studysync.dto.invitation.InvitationInfo.InvitationInfoResponseDto;
import org.studysync.studysync.service.invitation.InvitationService;
import org.studysync.studysync.swagger.ApiErrorCodeExample;
import org.studysync.studysync.swagger.ApiErrorCodeExamples;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invitation")
@Tag(name = "초대")
public class InvitationController {
    private final InvitationService invitationService;

    @Operation(summary = "초대코드 생성")
    @ApiErrorCodeExamples(value = {
            @ApiErrorCodeExample(value = HttpErrorCode.AccessDeniedError),
            @ApiErrorCodeExample(value = HttpErrorCode.NotValidAccessTokenError),
            @ApiErrorCodeExample(value = HttpErrorCode.ExpiredAccessTokenError),
            @ApiErrorCodeExample(value = HttpErrorCode.UserNotFoundError),
            @ApiErrorCodeExample(value = HttpErrorCode.NoSuchStudyGroupError),
            @ApiErrorCodeExample(value = HttpErrorCode.NoSuchStudyGroupMemberError),
    })
    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = InvitationResponseDto.class)))
    @PostMapping("/group/{groupId}")
    public ResponseEntity<InvitationResponseDto> inviteGroup(@PathVariable("groupId") String groupId, @AuthenticationPrincipal User user){
        InvitationDto dto = invitationService.inviteGroup(groupId, user);
        return new ResponseEntity<>(InvitationResponseDto.fromDto(dto), HttpStatus.CREATED);
    }


    @Operation(summary = "초대코드 조회")
    @ApiErrorCodeExamples(value = {
            @ApiErrorCodeExample(value = HttpErrorCode.AccessDeniedError),
            @ApiErrorCodeExample(value = HttpErrorCode.NotValidAccessTokenError),
            @ApiErrorCodeExample(value = HttpErrorCode.ExpiredAccessTokenError),
            @ApiErrorCodeExample(value = HttpErrorCode.UserNotFoundError),
            @ApiErrorCodeExample(value = HttpErrorCode.NoSuchInvitationCodeError),
            @ApiErrorCodeExample(value = HttpErrorCode.NoSuchStudyGroupError),
    })
    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = InvitationInfoResponseDto.class)))
    @GetMapping("/{code}")
    public ResponseEntity<InvitationInfoResponseDto> getInvitation(@PathVariable("code") String code){
        InvitationInfoDto dto = invitationService.getInvitation(code);
        return new ResponseEntity<>(InvitationInfoResponseDto.fromDto(dto), HttpStatus.OK);
    }


    @Operation(summary = "초대 수락")
    @ApiErrorCodeExamples(value = {
            @ApiErrorCodeExample(value = HttpErrorCode.AccessDeniedError),
            @ApiErrorCodeExample(value = HttpErrorCode.NotValidAccessTokenError),
            @ApiErrorCodeExample(value = HttpErrorCode.ExpiredAccessTokenError),
            @ApiErrorCodeExample(value = HttpErrorCode.UserNotFoundError),
            @ApiErrorCodeExample(value = HttpErrorCode.NoSuchInvitationCodeError),
            @ApiErrorCodeExample(value = HttpErrorCode.NoSuchStudyGroupError),
            @ApiErrorCodeExample(value = HttpErrorCode.AlreadyExistMemberError),
    })
    @PostMapping("/{code}/accept")
    public ResponseEntity<InvitationAcceptResponseDto> acceptInvitation(@PathVariable("code") String code, @AuthenticationPrincipal User user){
        InvitationAcceptDto dto = invitationService.acceptInvitation(code, user);
        return new ResponseEntity<>(InvitationAcceptResponseDto.fromDto(dto), HttpStatus.CREATED);
    }
}
