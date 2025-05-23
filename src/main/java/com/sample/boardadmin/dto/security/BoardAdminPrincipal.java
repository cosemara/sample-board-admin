package com.sample.boardadmin.dto.security;

import com.sample.boardadmin.domain.constant.RoleType;
import com.sample.boardadmin.dto.AdminAccountDto;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public record BoardAdminPrincipal(
    String username,
    String password,
    Collection<? extends GrantedAuthority> authorities,
    String email,
    String nickname,
    String memo,
    Map<String, Object> oAuth2Attiributes
    ) implements UserDetails, OAuth2User {

    public static BoardAdminPrincipal of(String username, String password, Set<RoleType> roleTypes, String email, String nickname, String memo) {
        return BoardAdminPrincipal.of(username, password, roleTypes, email, nickname, memo, Map.of());
    }

    public static BoardAdminPrincipal of(String username, String password, Set<RoleType> roleTypes, String email, String nickname, String memo, Map<String, Object> oAuth2Attiributes) {
        return new BoardAdminPrincipal(
            username,
            password,
            roleTypes.stream()
                     .map(RoleType::getRoleName)
                     .map(SimpleGrantedAuthority::new)
                     .collect(Collectors.toUnmodifiableSet())
            ,
            email,
            nickname,
            memo,
            oAuth2Attiributes
        );
    }

    public static BoardAdminPrincipal from(AdminAccountDto dto) {
        return BoardAdminPrincipal.of(
            dto.userId(),
            dto.userPassword(),
            dto.roleTypes(),
            dto.email(),
            dto.nickname(),
            dto.memo()
        );
    }

    public AdminAccountDto toDto() {
        return AdminAccountDto.of(
            username,
            password,
            authorities.stream()
                       .map(GrantedAuthority::getAuthority)
                       .map(RoleType::valueOf)
                       .collect(Collectors.toUnmodifiableSet()),
            email,
            nickname,
            memo
        );
    }


    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2Attiributes;
    }

    @Override
    public String getName() {
        return username;
    }

}