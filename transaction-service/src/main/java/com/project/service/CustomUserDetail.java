package com.project.service;

import com.project.payload.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetail implements UserDetails {
    UserDTO user;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetail(UserDTO user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    public static CustomUserDetail build(UserDTO user) {
        // System.out.println(user.getRoles());
        GrantedAuthority authorities = new SimpleGrantedAuthority(user.getRoles());

        return new CustomUserDetail(
                user,
                Collections.singleton(authorities));
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return user.getPassword();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getEmail() {
        return user.getId_email();
    }

    ;

    public String getRole() {
        return user.getRoles();
    }

    ;

    public UserDTO getUser() {
        return this.user;
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
        return true;
    }
}
