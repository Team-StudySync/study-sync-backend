package org.studysync.studysync.controller.user;

import lombok.RequiredArgsConstructor;
import org.studysync.studysync.domain.User;
import org.studysync.studysync.dto.user.UserInfo;
import org.studysync.studysync.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<UserInfo.Response> getUserInfo(@AuthenticationPrincipal User user) {
        UserInfo.Dto userInfoDto = userService.getUserInfo(user);
        return new ResponseEntity<>(UserInfo.Response.fromDto(userInfoDto), HttpStatus.OK);
    }
}
