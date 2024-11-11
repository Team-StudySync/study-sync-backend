package org.studysync.studysync.controller.temp;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.studysync.studysync.config.HttpErrorCode;
import org.studysync.studysync.dto.temp.login.TestLoginDto;
import org.studysync.studysync.dto.temp.login.TestLoginRequestDto;
import org.studysync.studysync.dto.temp.login.TestLoginResponseDto;
import org.studysync.studysync.dto.user.UserInfoResponseDto;
import org.studysync.studysync.service.temp.TempService;
import org.studysync.studysync.swagger.ApiErrorCodeExample;
import org.studysync.studysync.swagger.ApiErrorCodeExamples;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "테스트용 API")
public class TempController {
    private final TempService tempService;

    @Operation(summary = "테스트 유저 로그인")
    @ApiErrorCodeExamples(value = {
            @ApiErrorCodeExample(value = HttpErrorCode.AccessDeniedError),
            @ApiErrorCodeExample(value = HttpErrorCode.NotValidAccessTokenError),
            @ApiErrorCodeExample(value = HttpErrorCode.ExpiredAccessTokenError),
            @ApiErrorCodeExample(value = HttpErrorCode.UserNotFoundError)
    })
    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = TestLoginResponseDto.class)))
    @PostMapping("/testUser/token")
    public ResponseEntity<TestLoginResponseDto> loginTestUser(@Valid @RequestBody TestLoginRequestDto requestDto) {
        TestLoginDto dto = tempService.loginTestUser(requestDto);
        return new ResponseEntity<>(TestLoginResponseDto.fromDto(dto), HttpStatus.CREATED);

    }
}
