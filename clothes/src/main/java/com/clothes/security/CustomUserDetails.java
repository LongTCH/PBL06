package com.clothes.security;

import com.clothes.constant.AccountRolesEnum;
import com.clothes.model.Account;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    @Getter
    private final String username;
    @Getter
    private final String password;
    private final AccountRolesEnum role;

    public CustomUserDetails(Account account) {
        this.username = account.getEmail();
        this.password = account.getPassword();
        this.role = account.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }
}
