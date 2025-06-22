package org.personal.comerspleject.domain.users.admin.controller;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.users.admin.dto.request.AdminUpdateRequestDto;
import org.personal.comerspleject.domain.users.admin.dto.response.AdminResponseDto;
import org.personal.comerspleject.domain.users.admin.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecmos/admin/users")
public class AdminController {

    private final AdminService adminService;

    // 보안 처리
    @PreAuthorize("hasRole('ADMIN')")
    // 회원 전체 조회
    @GetMapping("/all")
    public ResponseEntity<List<AdminResponseDto>> getAllMembers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    // 회원 검색
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AdminResponseDto>> searchUser(@RequestParam String keyword) {
        return ResponseEntity.ok(adminService.searchUser(keyword));
    }

    // 회원 정보 수정
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUserInfo(@PathVariable Long userId,
                                                 @RequestBody AdminUpdateRequestDto adminUpdateRequestDto) {
        adminService.updateUserInfo(userId, adminUpdateRequestDto);
        return ResponseEntity.ok("회원 정보가 수정되었습니다");
    }

    // 회원 상세 조회
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<AdminResponseDto> getUserDetail(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getUserDetail(userId));
    }

    // 권한 변경
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}/role")
    public ResponseEntity<String> changeUserRole(@PathVariable Long userId,
                                                 @RequestParam String role) {
        adminService.changeUserRole(userId, role);
        return ResponseEntity.ok("권한이 변경되었습니다");
    }

    // 회원 삭제
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
