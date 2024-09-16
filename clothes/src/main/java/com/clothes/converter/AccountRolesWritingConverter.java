package com.clothes.converter;

import org.springframework.core.convert.converter.Converter;

import com.clothes.constant.AccountRolesEnum;

public class AccountRolesWritingConverter implements Converter<AccountRolesEnum, Integer> {
    @Override
    public Integer convert(AccountRolesEnum source) {
        return source.getValue();
    }
}
