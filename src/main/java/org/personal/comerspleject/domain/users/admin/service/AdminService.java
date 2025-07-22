package org.personal.comerspleject.domain.users.admin.service;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.config.exception.EcomosException;
import org.personal.comerspleject.config.exception.ErrorCode;
import org.personal.comerspleject.domain.users.admin.dto.request.AdminUpdateRequestDto;
import org.personal.comerspleject.domain.users.admin.dto.response.AdminResponseDto;
import org.personal.comerspleject.domain.users.seller.dto.response.ProductResponseDto;
import org.personal.comerspleject.domain.users.seller.repository.ProductRepository;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.personal.comerspleject.domain.users.user.entity.UserRole;
import org.personal.comerspleject.domain.users.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // 🔁 삭제되지 않은 유저 전체 조회
    public List<AdminResponseDto> getAllUsers() {
        List<User> users = userRepository.findByIsDeletedFalse();
        return users.stream()
                .map(AdminResponseDto::from)
                .collect(Collectors.toList());
    }

    // 🔁 삭제되지 않은 유저만 이름 또는 이메일로 검색
    public List<AdminResponseDto> searchUser(String keyword) {
        List<User> users = userRepository.findByIsDeletedFalseAndNameContainingOrEmailContaining(keyword, keyword);
        return users.stream()
                .map(AdminResponseDto::from)
                .collect(Collectors.toList());
    }

    // 유저 DB에서 완전 삭제
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_USER));
        userRepository.delete(user);
    }

    // 유저 정보 수정
    @Transactional
    public void updateUserInfo(Long userId, AdminUpdateRequestDto adminUpdateRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_USER));
        user.updateUserinfo(user.getPassword(), user.getAddress());
        user.setName(adminUpdateRequestDto.getName());
    }

    // 유저 상세 조회
    public AdminResponseDto getUserDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_USER));
        return AdminResponseDto.from(user);
    }

    // 회원 권한 변경
    @Transactional
    public void changeUserRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_USER));
        UserRole newRole = UserRole.of(role);
        user.updateUserRole(newRole);
    }

    // 특정 판매자 상품조회
    public List<ProductResponseDto> getProductBySellerId(Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_USER));

        if (!seller.getRole().equals(UserRole.SELLER)) {
            throw new EcomosException(ErrorCode._INVALID_USER_ROLE);
        }

        return productRepository.findBySellerUidAndIsDeletedFalse(sellerId).stream()
                .map(ProductResponseDto::from)
                .collect(Collectors.toList());
    }
}
