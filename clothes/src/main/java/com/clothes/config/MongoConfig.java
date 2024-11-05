package com.clothes.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
//    @Bean
//    MongoCustomConversions customConversions() {
//        List<Converter<?, ?>> converterList = new ArrayList<Converter<?, ?>>();
//        converterList.add(new AccountRolesReadingConverter());
//        converterList.add(new AccountRolesWritingConverter());
//        return new MongoCustomConversions(converterList);
//    }
}
