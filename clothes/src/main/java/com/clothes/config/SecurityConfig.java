package com.clothes.config;

import com.clothes.constant.AccountRolesEnum;
import com.clothes.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/seller/**").hasAuthority("SELLER")
//                        .requestMatchers("/customer/**").hasRole(AccountRolesEnum.CUSTOMER.name())
                        .requestMatchers("/customer/**").hasAuthority("CUSTOMER")

                        .requestMatchers("/**").permitAll()
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
                .exceptionHandling(e -> e.accessDeniedPage("/access-denied"))
                .sessionManagement(
                        sess -> sess.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void registerUser( String email, String password) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Email không hợp lệ.");
        }
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

    private boolean isValidEmail(String email) {
        String gmailRegex = "^[a-zA-Z0-9](?!.*\\.\\.)[a-zA-Z0-9._]*[a-zA-Z0-9]@gmail\\.com$";
        return email.matches(gmailRegex);
    }

}
