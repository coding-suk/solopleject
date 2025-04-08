package org.personal.comerspleject.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.jwt.AuthUser;
import org.personal.comerspleject.domain.user.dto.DeleteUserRequestDto;
import org.personal.comerspleject.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @DeleteMapping()
    public ResponseEntity<String> deletedUSer(@AuthenticationPrincipal AuthUser authUser,
                                              @RequestBody DeleteUserRequestDto deleteUserRequestDto) {
        userService.deletedUser(authUser, deleteUserRequestDto);
        return ResponseEntity.ok("회원탈퇴가 완료되었습니다");
    }

}
