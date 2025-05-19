package kr.hhplus.be.server;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.Random.class)
public abstract class IntegrationTestSupport {

    @Autowired
    private DbCleaner dbCleaner;
    @Autowired
    private RedisTemplate redisContainer;

    @BeforeEach
    public void setUp() {
        dbCleaner.execute();
    }

}
