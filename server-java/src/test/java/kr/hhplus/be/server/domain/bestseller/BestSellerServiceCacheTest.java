package kr.hhplus.be.server.domain.bestseller;

import kr.hhplus.be.server.config.cache.CacheKey;
import kr.hhplus.be.server.config.cache.CacheManagerName;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class BestSellerServiceCacheTest {

    @MockitoBean
    private BestSellerRepository bestSellerRepository;

    @Autowired
    private BestSellerService bestSellerService;

    @Autowired
    @Qualifier(CacheManagerName.LOCAL)
    CacheManager cacheManager;

    @BeforeEach
    void tearDown() {
        cacheManager.getCache(CacheKey.BEST_SELLERS).clear();
    }

    @Test
    @DisplayName("캐시가 비어있을 때, 오늘의 베스트셀러를 조회하면 DB에서 조회하고 캐시에 저장된다.")
    void 캐시가_비어있을_때_오늘의_베스트셀러를_조회하면_DB에서_조회하고_캐시에_저장된다() {
        // given
        when(bestSellerRepository.getTop5BestSellersByDate(any()))
                .thenReturn(List.of(
                        Instancio.of(BestSeller.class).create(),
                        Instancio.of(BestSeller.class).create(),
                        Instancio.of(BestSeller.class).create(),
                        Instancio.of(BestSeller.class).create(),
                        Instancio.of(BestSeller.class).create()
                ));

        // when
        List<BestSeller> result = bestSellerService.getTodayTop5BestSellers();

        // then
        Cache cache = cacheManager.getCache(CacheKey.BEST_SELLERS);
        assertNotNull(cache);
        List<BestSeller> cachedValue = cache.get(CacheKey.BEST_SELLERS, List.class);
        assertNotNull(cachedValue);
        assertIterableEquals(
                result.stream().map(BestSeller::getId).toList(),
                cachedValue.stream().map(BestSeller::getId).toList()
        );
    }

    @Test
    @DisplayName("캐시가 비어있지 않을 때, 오늘의 베스트셀러를 조회하면 캐시에서 조회된다.")
    void 캐시가_비어있지_않을_때_오늘의_베스트셀러를_조회하면_캐시에서_조회된다() {
        // given
        List<BestSeller> cachedBestSellers = List.of(
                Instancio.of(BestSeller.class).create(),
                Instancio.of(BestSeller.class).create(),
                Instancio.of(BestSeller.class).create(),
                Instancio.of(BestSeller.class).create(),
                Instancio.of(BestSeller.class).create()
        );
        Cache cache = cacheManager.getCache(CacheKey.BEST_SELLERS);
        assertNotNull(cache);
        cache.put(CacheKey.BEST_SELLERS, cachedBestSellers);

        // when
        List<BestSeller> result = bestSellerService.getTodayTop5BestSellers();

        // then
        // DB 조회가 발생하지 않아야 함
        verifyNoInteractions(bestSellerRepository);
        assertSame(cachedBestSellers, result);
    }

    @Test
    @DisplayName("refreshTop5BestSellersCache 메서드를 호출하면 캐시가 갱신된다.")
    void refreshTop5BestSellersCache() {
        // given
        List<BestSeller> cachedBestSellers = List.of(
                Instancio.of(BestSeller.class).create(),
                Instancio.of(BestSeller.class).create(),
                Instancio.of(BestSeller.class).create(),
                Instancio.of(BestSeller.class).create(),
                Instancio.of(BestSeller.class).create()
        );
        List<BestSeller> refreshedBestSellers = List.of(
                Instancio.of(BestSeller.class).create(),
                Instancio.of(BestSeller.class).create(),
                Instancio.of(BestSeller.class).create(),
                Instancio.of(BestSeller.class).create(),
                Instancio.of(BestSeller.class).create()
        );
        cacheManager.getCache(CacheKey.BEST_SELLERS).put(CacheKey.BEST_SELLERS, cachedBestSellers);
        when(bestSellerRepository.getTop5BestSellersByDate(any()))
                .thenReturn(refreshedBestSellers);

        // when
        List<BestSeller> result = bestSellerService.refreshTop5BestSellersCache();

        // then
        Cache cache = cacheManager.getCache(CacheKey.BEST_SELLERS);
        assertNotNull(cache);
        List<BestSeller> cachedValue = cache.get(CacheKey.BEST_SELLERS, List.class);
        assertNotNull(cachedValue);
        assertIterableEquals(
                result.stream().map(BestSeller::getId).toList(),
                cachedValue.stream().map(BestSeller::getId).toList()
        );
    }

}