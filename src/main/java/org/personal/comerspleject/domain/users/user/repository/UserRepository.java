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

    List<User> findByNameContainingOrEmailContaining(String keyword, String keyword1);

//    Collection<User> findAll();
}
