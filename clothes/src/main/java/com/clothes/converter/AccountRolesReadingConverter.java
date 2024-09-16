package com.clothes.converter;

import org.springframework.core.convert.converter.Converter;

import com.clothes.constant.AccountRolesEnum;

public class AccountRolesReadingConverter implements Converter<Integer, AccountRolesEnum> {
    @Override
    public AccountRolesEnum convert(Integer source) {
        return AccountRolesEnum.fromValue(source);
    }
}
