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

    // 유저 조회(전체)
    public List<AdminResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(AdminResponseDto::from)
                .collect(Collectors.toList());
    }

    // 유저 검색(이름 or 이메일)
    public List<AdminResponseDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContainingOrEmailContaining(keyword, keyword);
        return users.stream().map(AdminResponseDto::from).collect(Collectors.toList());
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

    // 특정 판매자 상품조회
    public List<ProductResponseDto> getProductBySellerId(Long sellerId) {
        // 판매자 존재 여부
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new EcomosException(ErrorCode._NOT_FOUND_USER));

        if(!seller.getRole().equals(UserRole.SELLER)) {
            throw new EcomosException(ErrorCode._INVALID_USER_ROLE);
        }
        return productRepository.findBySellerId(sellerId).stream()
                .filter(p-> !p.isDeleted())// 삭제된  상품 제외
                .map(ProductResponseDto::from)
                .collect(Collectors.toList());
    }
}
