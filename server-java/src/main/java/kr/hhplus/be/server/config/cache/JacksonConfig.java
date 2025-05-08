package kr.hhplus.be.server.config.cache;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Java 8+ 시간 타입 처리
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        StdTypeResolverBuilder builder = new ObjectMapper.DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping.EVERYTHING,
                objectMapper.getPolymorphicTypeValidator());
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        objectMapper.setDefaultTyping(builder);

        // PageImpl에 대한 커스텀 직렬/역직렬화기 등록
        SimpleModule module = new SimpleModule();
        module.addSerializer(Page.class, new PageSerializer());
        module.addDeserializer(PageImpl.class, new PageDeserializer());
        objectMapper.registerModule(module);

        return objectMapper;
    }
}
