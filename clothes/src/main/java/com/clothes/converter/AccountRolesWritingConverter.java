package com.clothes.converter;

import com.clothes.constant.AccountRolesEnum;
import org.springframework.core.convert.converter.Converter;

public class AccountRolesWritingConverter implements Converter<AccountRolesEnum, Integer> {
    @Override
    public Integer convert(AccountRolesEnum source) {
        return source.getValue();
    }
}
