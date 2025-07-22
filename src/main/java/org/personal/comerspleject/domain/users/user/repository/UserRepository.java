package org.personal.comerspleject.domain.users.user.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(@NotBlank @Email String email);

    boolean existsByEmail(@NotBlank @Email String email);

    // 삭제되지 않은 사용자만 검섹
    List<User> findByIsDeletedFalse();

    // 이름 또는 이메일로 검색하면서 삭제되지 않은 유저만 검색
    List<User> findByIsDeletedFalseAndNameContainingOrEmailContaining(String name, String email);
}
