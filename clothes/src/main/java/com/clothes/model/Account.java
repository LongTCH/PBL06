package com.clothes.model;

import com.clothes.constant.AccountRolesEnum;
import com.clothes.model.base.AuditableEntity;
import com.clothes.model.embedded.Address;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
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

}
