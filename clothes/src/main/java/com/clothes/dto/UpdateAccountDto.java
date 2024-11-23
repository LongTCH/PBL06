package com.clothes.dto;

import com.clothes.model.embedded.Address;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateAccountDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Address address;

    public UpdateAccountDto(String firstName, String lastName, String email, String phone, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}
