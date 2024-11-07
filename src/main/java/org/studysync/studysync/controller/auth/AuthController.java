package org.studysync.studysync.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.studysync.studysync.dto.auth.login.Login;
import org.studysync.studysync.dto.auth.logout.Logout;
import org.studysync.studysync.dto.auth.tokenReissue.TokenReIssue;
import org.studysync.studysync.service.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Login.Response> login(@Valid @RequestBody Login.Request requestDto) {
        Login.Dto dto = authService.login(requestDto);
        return new ResponseEntity<>(Login.Response.fromDto(dto), HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<Logout.Response> logout(
            @RequestHeader("Authorization-refresh") String refreshToken
    ) {
        authService.logout(refreshToken);
        return new ResponseEntity<>(Logout.Response.success(), HttpStatus.CREATED);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<TokenReIssue.Response> reIssueToken(
            @RequestHeader("Authorization") String accessToken,
            @RequestHeader("Authorization-refresh") String refreshToken
    ){
        TokenReIssue.Dto dto = authService.reIssueToken(accessToken, refreshToken);
        return new ResponseEntity<>(TokenReIssue.Response.fromDto(dto), HttpStatus.CREATED);
    }
}
