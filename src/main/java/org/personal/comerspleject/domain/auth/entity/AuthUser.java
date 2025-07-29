package org.personal.comerspleject.domain.auth.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.personal.comerspleject.domain.users.user.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AuthUser implements UserDetails {

    /* 토큰에 유저정보를 가져와서 권한을 확인하기 위한 자료
    인증하는 곳에서는 중요한 정보를 넣으면 안됌
    로그인 정보
     */
    private final Long id;
    private final String name;
    private final String email;
    private final UserRole userRole;
    private final String address;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return null; // 이미 로그인된 상태로 사용할 경우 null
    }

    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }

}
