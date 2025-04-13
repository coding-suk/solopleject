package org.personal.comerspleject.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.config.jwt.AuthUser;
import org.personal.comerspleject.domain.user.dto.request.ResetPasswordRequestDto;
import org.personal.comerspleject.domain.user.dto.request.UpdateUserRequestDto;
import org.personal.comerspleject.domain.user.dto.response.UpdateUserResponseDto;
import org.personal.comerspleject.domain.user.dto.request.DeleteUserRequestDto;
import org.personal.comerspleject.domain.user.entity.User;
import org.personal.comerspleject.domain.user.entity.UserRole;
import org.personal.comerspleject.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void deletedUser(AuthUser authUser, DeleteUserRequestDto deleteUserRequestDto) {

        System.out.print("AuthUserEmail: " + authUser.getEmail());
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_USER));

        if(!passwordEncoder.matches(deleteUserRequestDto.getPassword(), user.getPassword())) {
            throw new EcomosException(ErrorCode._PASSWORD_NOT_MATCHES);
        }

        if(user.getIsdeleted()) {
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

        if(user.getIsdeleted()) {
            throw new EcomosException(ErrorCode._DELETED_USER);
        }
        user.updateUserRole(UserRole.ADMIN);
    }

    // 회원 정보 수정 로직
    @Transactional
    public UpdateUserResponseDto updateUser(String email, UpdateUserRequestDto updateUserRequestDto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_USER));

        if(user.getIsdeleted()) {
            throw new EcomosException(ErrorCode._DELETED_USER);
        }

        String encodedPassword = passwordEncoder.encode(updateUserRequestDto.getPassword());
        user.updateUserinfo(encodedPassword, updateUserRequestDto.getAddress());

        return new UpdateUserResponseDto(user.getEmail(), user.getAddress());
    }

    // 비밀번호 찾기
    public void resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {
        User user = userRepository.findByEmail(resetPasswordRequestDto.getEmail())
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_USER));

        if(user.getIsdeleted()) {
            throw new EcomosException(ErrorCode._DELETED_USER);
        }

        String encodedPassword = passwordEncoder.encode(resetPasswordRequestDto.getNewPassword());
        user.updatePassword(encodedPassword);

        userRepository.save(user);
    }

    public void sendPasswordEmail(String email) {
    }
}
