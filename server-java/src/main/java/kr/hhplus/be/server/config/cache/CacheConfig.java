package kr.hhplus.be.server.config.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {

    private final ObjectMapper objectMapper;

    @Bean(CacheManagerName.LOCAL)
    public CacheManager localCacheManager() {
        CaffeineCacheManager cm = new CaffeineCacheManager();
        caffeineConfigs().forEach((k, v) -> cm.registerCustomCache(k, v.build())); // CaffeineCacheManager에 Caffeine 캐시 설정 등록
        return cm;
    }

    private Map<String, Caffeine<Object, Object>> caffeineConfigs() {
        Map<String, Caffeine<Object, Object>> caffeineConfig = new ConcurrentHashMap<>();
        caffeineConfig.put("default", Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterAccess(5, TimeUnit.MINUTES));
        caffeineConfig.put(CacheKey.BEST_SELLERS, Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterAccess(25, TimeUnit.HOURS)
                .recordStats()
        );
        return caffeineConfig;
    }

    @Primary
    @Bean(CacheManagerName.GLOBAL)
    public CacheManager globalCacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfiguration())
                .withInitialCacheConfigurations(redisConfig())
                .transactionAware() // Redis 트랜잭션 지원
                .build();
    }

    private Map<String, RedisCacheConfiguration> redisConfig() {
        Map<String, RedisCacheConfiguration> cacheConfigurationMap = new HashMap<>();
        cacheConfigurationMap.put(CacheKey.PRODUCTS, defaultConfiguration().entryTtl(Duration.ofSeconds(30)));
        cacheConfigurationMap.put(CacheKey.PRODUCT, defaultConfiguration().entryTtl(Duration.ofSeconds(30)));
        return cacheConfigurationMap;
    }

    private RedisCacheConfiguration defaultConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)))
                .entryTtl(Duration.ofMinutes(5));
    }
}
