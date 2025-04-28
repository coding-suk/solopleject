package org.personal.comerspleject.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.admin.dto.response.AdminResponseDto;
import org.personal.comerspleject.domain.admin.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ecmos/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<AdminResponseDto>> getAllMembers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

}
