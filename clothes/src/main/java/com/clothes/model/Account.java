package com.clothes.model;

import com.clothes.constant.AccountRolesEnum;
import com.clothes.model.base.AuditableEntity;
import com.clothes.model.embedded.Address;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accounts")
public class Account extends AuditableEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String phone;
    private String firstName;
    private String lastName;
    private Address address;
    private AccountRolesEnum role = AccountRolesEnum.CUSTOMER;
    private boolean locked = false;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

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
