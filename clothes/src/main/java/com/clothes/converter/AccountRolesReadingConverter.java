package com.clothes.converter;

import com.clothes.constant.AccountRolesEnum;
import org.springframework.core.convert.converter.Converter;

public class AccountRolesReadingConverter implements Converter<Integer, AccountRolesEnum> {
    @Override
    public AccountRolesEnum convert(Integer source) {
        return AccountRolesEnum.fromValue(source);
    }
}
