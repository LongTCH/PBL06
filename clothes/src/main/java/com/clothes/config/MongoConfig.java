package com.clothes.config;

import com.clothes.converter.AccountRolesReadingConverter;
import com.clothes.converter.AccountRolesWritingConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
    @Bean
    MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<Converter<?, ?>>();
        converterList.add(new AccountRolesReadingConverter());
        converterList.add(new AccountRolesWritingConverter());
        return new MongoCustomConversions(converterList);
    }
}
