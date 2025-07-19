package org.personal.comerspleject.domain.users.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column
    private UserRole role;

    private String address;

    private int point = 0;

    // 회원탈퇴 유무
    private boolean isdeleted = false;

    // 회원가입 당시
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 자동 설정
    @PrePersist
    public void perPersist() {
        this.createdAt = LocalDateTime.now();
    }

    // 생일 쿠폰 발급
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    public User(String email, String password, String name, UserRole role, String address, LocalDate birthDate) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.address = address;
        this.birthDate = birthDate;
    }

    // AuthUser로 User 만들 때
    public User(String email, String password, String address) {
        this.email = email;
        this.password = password;
        this.address = address;
    }

    private User(Long uid, String email, UserRole role) {
        this.uid = uid;
        this.email = email;
        this.role = role;
    }

    public void deletedUser(String email, String password) {
        this.isdeleted = true;
    }

    public void updateUserRole(UserRole newUserRole) {
        this.role = newUserRole;
    }

    // User정보 수정 메서드
    public void updateUserinfo(String password, String address) {
        this.password = password;
        this.address = address;
    }

    // 비밀번호 수정
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void addPoint(int reword) {
        this.point += reword;
    }

}
