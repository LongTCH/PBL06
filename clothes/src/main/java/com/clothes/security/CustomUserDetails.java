package com.clothes.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.clothes.constant.AccountRolesEnum;
import com.clothes.model.Account;

public class CustomUserDetails implements UserDetails {
    private String username;
    private String password;
    private AccountRolesEnum role;

    public CustomUserDetails(Account account) {
        this.username = account.getEmail();
        this.password = account.getPassword();
        this.role = account.getRole();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }
}
