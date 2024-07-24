package com.sparta.studytrek.security;

import com.sparta.studytrek.domain.auth.entity.Role;
import com.sparta.studytrek.domain.auth.entity.User;
import com.sparta.studytrek.domain.auth.entity.UserRoleEnum;
import com.sparta.studytrek.domain.auth.entity.UserStatusEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = user.getRole();
        UserRoleEnum roleEnum = role.getRole();
        String authority = roleEnum.getUserRole();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        // UserStatusEnum 이 승인된 상태인 경우 추가 권한을 부여
        UserStatusEnum statusEnum = user.getStatus();
        if (statusEnum == UserStatusEnum.APPROVER) {
            authorities.add(new SimpleGrantedAuthority("ROLE_APPROVER"));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // UserStautsEnum에 따라 계정 활성화 여부 결정
        // APPROVER 상태일 때만 true 반환
        return user.getStatus() == UserStatusEnum.APPROVER;
    }

    // 사용자의 현재 상태를 확인하는 메서드
    public UserStatusEnum getUserStatus() {
        return user.getStatus();
    }

    // 사용자가 승인된 상태인지 확인하는 메서드
    public boolean isApproved() {
        return user.getStatus() == UserStatusEnum.APPROVER;
    }
}