package com.clothes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.clothes.constant.AccountRolesEnum;
import java.io.Serializable;

@Document(collection = "accounts")
public class Account implements Serializable {
    @Id
    private String id;
    private String email;
    private String password;
    private String phone;
    private AccountRolesEnum role = AccountRolesEnum.CUSTOMER;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AccountRolesEnum getRole() {
        return role;
    }

    public void setRole(AccountRolesEnum role) {
        this.role = role;
    }
}
