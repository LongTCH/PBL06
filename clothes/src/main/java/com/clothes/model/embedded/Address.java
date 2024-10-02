package com.clothes.model.embedded;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Address implements Serializable {
    private String city;
    private String district;
    private String ward;
    private String street;

}
