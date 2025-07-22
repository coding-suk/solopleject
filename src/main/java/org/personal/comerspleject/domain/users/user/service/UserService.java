package org.personal.comerspleject.domain.users.user.service;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.auth.entity.AuthUser;
import org.personal.comerspleject.domain.users.user.dto.request.UpdateUserRequestDto;
import org.personal.comerspleject.domain.users.user.dto.request.afterlogin.ChangePasswordRequestDto;
import org.personal.comerspleject.domain.users.user.dto.response.UpdateUserResponseDto;
import org.personal.comerspleject.domain.users.user.dto.request.DeleteUserRequestDto;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.personal.comerspleject.domain.users.user.entity.UserRole;
import org.personal.comerspleject.domain.users.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EmailService emailService;

    @Value("${server.base-url}")
    private String serverBaseUrl;

    @Transactional
    public void deletedUser(AuthUser authUser, DeleteUserRequestDto deleteUserRequestDto) {

        System.out.print("AuthUserEmail: " + authUser.getEmail());
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_USER));

        if(!passwordEncoder.matches(deleteUserRequestDto.getPassword(), user.getPassword())) {
            throw new EcomosException(ErrorCode._PASSWORD_NOT_MATCHES);
        }

        if(user.isDeleted()) {
            throw new EcomosException(ErrorCode._DELETED_USER);
        }

        user.deletedUser(user.getEmail(), user.getPassword());

        userRepository.save(user);
    }

    // 회원의 권한을 admin으로 승격 시키는 로직
    @Transactional
    public void updateUserAuthority(Long id, AuthUser authUser) {

        boolean isAdmin = authUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));

        if(!isAdmin) {
            throw new EcomosException(ErrorCode._NOT_PERMITTED_USER);
        }
        User user = userRepository.findById(id).orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_USER));

        if(user.isDeleted()) {
            throw new EcomosException(ErrorCode._DELETED_USER);
        }
        user.updateUserRole(UserRole.ADMIN);
    }

    // 회원 정보 수정 로직
    @Transactional
    public UpdateUserResponseDto updateUser(String email, UpdateUserRequestDto updateUserRequestDto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_USER));

        if(user.isDeleted()) {
            throw new EcomosException(ErrorCode._DELETED_USER);
        }

        String encodedPassword = passwordEncoder.encode(updateUserRequestDto.getPassword());
        user.updateUserinfo(encodedPassword, updateUserRequestDto.getAddress());

        return new UpdateUserResponseDto(user.getEmail(), user.getAddress());
    }

    // 로그인후 비밀번호 변경
    public void changePassword(String email, ChangePasswordRequestDto changePasswordRequestDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_USER));

        if(user.isDeleted()) {
            throw new EcomosException(ErrorCode._DELETED_USER);
        }

        if(!passwordEncoder.matches(changePasswordRequestDto.getCurrentPassword(), user.getPassword())) {
            throw new EcomosException(ErrorCode._PASSWORD_NOT_MATCHES);
        }
        user.updatePassword(passwordEncoder.encode(changePasswordRequestDto.getNewPassword()));
        userRepository.save(user);
    }

    // 이메일 전송을 통한 비밀번호 변경
    public void sendPasswordEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_USER));

        if(user.isDeleted()) {
            throw new EcomosException(ErrorCode._DELETED_USER);
        }
        String token = tokenService.generatePasswordResetToken(email);// 토큰 생성

        String resetLink = serverBaseUrl + "/ecomos/users/pasword/verify?token=" + token;

        String subject = "[이커머스] 비밀번호 재설정 안내";
        String body = "<h3>비밀번호 재설정을 원하신다면 아래 링크를 클릭해주세요.</h3>" +
                "<a href='" + resetLink + "'>비밀번호 재설정 링크</a>" +
                "<br><br><small>해당 링크는 30분 동안 유효합니다.</small>";

        emailService.sendEmail(email, subject, body);
    }

    public void fixPassword(String token, String newPassword) {
        if(!tokenService.isValid(token)) {
            throw new EcomosException(ErrorCode._INVALID_TOKEN);
        }

        String email = tokenService.getEmailFromToken(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new EcomosException(ErrorCode._NOT_FOUND_USER));

        // 탈퇴한 사용자는 제외
        if(user.isDeleted()) {
            throw new EcomosException(ErrorCode._DELETED_USER);
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodedPassword);

        tokenService.removeToken(token);
    }
}
