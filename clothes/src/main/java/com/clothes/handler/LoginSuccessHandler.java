package com.clothes.handler;

import com.clothes.constant.AccountRolesEnum;
import com.clothes.repository.AccountsRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private AccountsRepository accountsRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        var account = accountsRepository.findByEmail(authentication.getName());
        HttpSession session = request.getSession();
        session.setAttribute("account", account.get());
        if (account.get().getRole().equals(AccountRolesEnum.ADMIN)) {
            response.sendRedirect("/admin");
            return;
        } else if (account.get().getRole().equals(AccountRolesEnum.SELLER)) {
            response.sendRedirect("/seller");
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("/");
    }

}
