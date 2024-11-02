package com.project.ecommerce.service.admin;

import com.project.ecommerce.model.Roles;
import com.project.ecommerce.model.Users;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
@AllArgsConstructor
@NoArgsConstructor
public class MyUserDetails implements UserDetails {
    private Users users;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Roles> roles = users.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Roles role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getUsername();
    }
    public Users getUsers() {
        return this.users;
    }
    @Override
    public boolean isEnabled() {
        return users.isEnable();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }
}
