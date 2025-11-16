package com.fpt.exam.config;

import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper mapper = builder.build();
        
        // Tăng max nesting depth để tránh lỗi khi serialize nested objects
        StreamWriteConstraints constraints = StreamWriteConstraints.builder()
                .maxNestingDepth(2000)
                .build();
        mapper.getFactory().setStreamWriteConstraints(constraints);
        
        // Disable FAIL_ON_EMPTY_BEANS
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        
        // Register JavaTimeModule để xử lý Date/Time
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        return mapper;
    }
}

