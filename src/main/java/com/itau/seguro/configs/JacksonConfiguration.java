package com.itau.seguro.configs;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.TimeZone;

@Configuration
public class JacksonConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return new Jackson2ObjectMapperBuilderCustomizer() {
            
            @Override
            public void customize(Jackson2ObjectMapperBuilder builder) {
                builder.timeZone(TimeZone.getDefault());
            }
        };
    }
}
