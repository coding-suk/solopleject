package org.personal.comerspleject.domain.users.user.controller;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.common.dto.response.CommonResponseDto;
import org.personal.comerspleject.domain.auth.entity.AuthUser;
import org.personal.comerspleject.domain.users.user.dto.request.afterlogin.ChangePasswordRequestDto;
import org.personal.comerspleject.domain.users.user.dto.request.DeleteUserRequestDto;
import org.personal.comerspleject.domain.users.user.dto.request.sendemail.NeedEmailResetPassword;
import org.personal.comerspleject.domain.users.user.dto.request.sendemail.SendPasswordEmailRequestDto;
import org.personal.comerspleject.domain.users.user.service.TokenService;
import org.personal.comerspleject.domain.users.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecomos/users")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

    @DeleteMapping()
    public ResponseEntity<String> deletedUSer(@AuthenticationPrincipal AuthUser authUser,
                                              @RequestBody DeleteUserRequestDto deleteUserRequestDto) {
        userService.deletedUser(authUser, deleteUserRequestDto);
        return ResponseEntity.ok("회원탈퇴가 완료되었습니다");
    }

    // 로그인을 통한 비밀번호를 직접 변경
    @PostMapping("/me/change")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal AuthUser authUser,
                                                 @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        userService.changePassword(authUser.getEmail(), changePasswordRequestDto);

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다");
    }

    // 비밀번호 수정 이메일 버전(로그인 전)
    @PostMapping("/password/email")
    public ResponseEntity<CommonResponseDto> sendPasswordEmail(@RequestBody SendPasswordEmailRequestDto sendPasswordRequestDto) {
        userService.sendPasswordEmail(sendPasswordRequestDto.getEmail());

        return ResponseEntity.ok(new CommonResponseDto("비밀번호 재설정 이메일이 전송 되었습니다"));
    }

    // 토큰 인증 후 비밀번호 입력 페이지 반환(간단 HTML)
    @PostMapping("/pasword/verify")
    public ResponseEntity<String> verifyToken(@RequestParam("token") String token) {
        if (!tokenService.isValid(token)) {
            return ResponseEntity.badRequest().body("<h3>유효하지 않은 또는 만료된 토큰입니다.</h3>");
        }

        // 간단한 HTML 입력 폼 제공
        return ResponseEntity.ok(
                "<form method='POST' action='/ecomos/users/password/reset'>" +
                        "<input type='hidden' name='token' value='" + token + "'/>" +
                        "새 비밀번호: <input type='password' name='newPassword1'/>" +
                        "<button type='submit'>비밀번호 변경</button>" +
                        "</form>"
        );
    }

    @PostMapping("/password/reset")
    public ResponseEntity<String> resetPassword(@RequestBody NeedEmailResetPassword resetPasswordRequestDto) {
        userService.fixPassword(resetPasswordRequestDto.getToken(), resetPasswordRequestDto.getNewPassword1());

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

}
