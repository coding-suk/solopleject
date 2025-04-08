package org.personal.comerspleject.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.config.jwt.AuthUser;
import org.personal.comerspleject.domain.user.dto.DeleteUserRequestDto;
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

    @Transactional
    public void updateUserAuthority(Long id, AuthUser authUser) {
        if(!authUser.getAuthorities().equals(UserRole.ADMIN)) {
            throw new EcomosException(ErrorCode._NOT_PERMITTED_USER);
        }
        User user = userRepository.findById(id).orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_USER));

        if(user.getIsdeleted()) {
            throw new EcomosException(ErrorCode._DELETED_USER);
        }
        user.updateUserRole(UserRole.ADMIN);
    }

}
