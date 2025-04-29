package org.personal.comerspleject.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.admin.dto.request.AdminUpdateRequestDto;
import org.personal.comerspleject.domain.admin.dto.response.AdminResponseDto;
import org.personal.comerspleject.domain.user.entity.User;
import org.personal.comerspleject.domain.user.entity.UserRole;
import org.personal.comerspleject.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    // 유저 조회(전체)
    public List<AdminResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(AdminResponseDto::from)
                .collect(Collectors.toList());
    }

    // 유저 검색(이름 or 이메일)
    public List<AdminResponseDto> searchUser(String keyword) {
        List<User> users = userRepository.findByAll().stream()
                .filter(user -> user.getEmail().contains(keyword) || user.getName().contains(keyword))
                .collect(Collectors.toList());
        return users.stream().map(AdminResponseDto :: from).collect(Collectors.toList());
    }

    // 유저 제거
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_USER));

        userRepository.delete(user); // DB에서 삭제
    }

    // 유저 정보 수정
    @Transactional
    public void updateUserInfo(Long userId, AdminUpdateRequestDto adminUpdateRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_USER));
        user.updateUserinfo(user.getPassword(), user.getAddress());
        user.setName(adminUpdateRequestDto.getName());
    }

    // 유저 상세 조회
    public AdminResponseDto getUserDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EcomosException(ErrorCode._DELETED_USER));
        return AdminResponseDto.from(user);
    }

    // 회원 권한 변경
    @Transactional
    public void changeUserRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EcomosException(ErrorCode._NOT_FOUND_USER));
        UserRole newRole = UserRole.of(role);
        user.updateUserRole(newRole);
    }
}
