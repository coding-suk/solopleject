package org.personal.comerspleject.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.aot.generate.GeneratedTypeReference;

import java.io.Serializable;

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

    // 회원탈퇴 유무
    private Boolean isdeleted = false;

    public User(String email, String password, String name, UserRole role, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.address = address;
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

    // User 수정 메서드
    public void updateUserinfo(String password, String address) {
        this.password = password;
        this.address = address;
    }
}
