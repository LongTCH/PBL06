package com.clothes.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePassDto {
    private String email;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    public ChangePassDto(String email, String oldPassword, String newPassword, String confirmPassword) {
        this.email = email;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
}
