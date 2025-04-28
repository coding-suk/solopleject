package org.personal.comerspleject.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.admin.dto.response.AdminResponseDto;
import org.personal.comerspleject.domain.user.entity.User;
import org.personal.comerspleject.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public List<AdminResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(AdminResponseDto::from)
                .collect(Collectors.toList());
    }

}
