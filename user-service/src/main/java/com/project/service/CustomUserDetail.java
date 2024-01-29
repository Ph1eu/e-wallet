package com.project.service;
import com.project.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.*;

public class CustomUserDetail implements UserDetails   {
    User user;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetail(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }
    public static CustomUserDetail build(User user) {
        GrantedAuthority authorities =new SimpleGrantedAuthority(user.getRoles().getName().name());

        return new CustomUserDetail(
                user,
                Collections.singleton(authorities));
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    public String getPassword(){
        return user.getPassword();
    }
    public String getUsername(){
        return user.getUsername();
    }

    public String getEmail(){return user.getId_email();};
    public String getRole(){return user.getRoles().getName().name();};

    public User getUser() {
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
