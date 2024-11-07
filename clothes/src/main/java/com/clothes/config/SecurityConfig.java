package com.clothes.config;

import com.clothes.constant.AccountRolesEnum;
import com.clothes.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/admin/**").hasRole(AccountRolesEnum.ADMIN.name())
                        .requestMatchers("/seller/**").hasRole(AccountRolesEnum.SELLER.name())
                        .requestMatchers("/customer/**").hasRole(AccountRolesEnum.CUSTOMER.name())
                        .anyRequest().authenticated())
                .formLogin(f -> f.loginPage("/sessions/login").permitAll()
                        .loginProcessingUrl("/sessions/login")
                        .defaultSuccessUrl("/")
                        .successHandler(loginSuccessHandler)
                        .failureUrl("/sessions/login?error=true"))
                .logout(f -> f.logoutUrl("/sessions/logout")
                        .logoutSuccessUrl("/sessions/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("SESSION", "JSESSIONID"))
                .sessionManagement(
                        sess -> sess.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void registerUser(String password) {
        if (isValidPassword(password)) {
            String encodedPassword = passwordEncoder().encode(password);
        } else {
            throw new IllegalArgumentException("Mật khẩu không hợp lệ. Mật khẩu ít nhất 6 ký tự, bao gồm chữ cái.");
        }
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6 &&
                password.matches(".*[a-zA-Z].*") &&
                password.matches(".*[0-6].*");
    }
}
