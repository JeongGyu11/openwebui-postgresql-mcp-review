package kr.fasol.obix.admin.config;

import kr.fasol.obix.admin.common.cache.CustomCacheEntryListener;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;

/**
 * 시스템명 :
 * 업무구분명 : 설정 > Cache 설정
 * 프로그램명 : Cache 설정
 * 설명 :
 *
 * 클래스 : CacheManagerConfig.java
 * 작성자 : 정영탁
 * 작성일 : 2024. 11. 12.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2024. 11. 12.        정영탁			  최초작성
 */
@Configuration
@EnableCaching
public class CacheManagerConfig {

    @Bean
    public javax.cache.CacheManager jCacheManager() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        return cachingProvider.getCacheManager();
    }

    @Bean
    public org.springframework.cache.CacheManager cacheManager(javax.cache.CacheManager jCacheManager) {

        // Cache Entry Listener Configuration
        MutableCacheEntryListenerConfiguration<Object, Object> cacheEntryListenerConfig =
                new MutableCacheEntryListenerConfiguration<>(
                        FactoryBuilder.factoryOf(CustomCacheEntryListener.class),
                        null, // CacheEntryEventFilter (필터가 필요 없는 경우 null)
                        true, // 동기 설정
                        true  // 오래된 값 필요 여부
                );

        javax.cache.configuration.Configuration<Object, Object> configuration = new MutableConfiguration<>()
                .addCacheEntryListenerConfiguration(cacheEntryListenerConfig) // 캐시에 리스너 구성 추가
                .setTypes(Object.class, Object.class)
                .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_DAY))
                .setStoreByValue(false)
                .setStatisticsEnabled(true);

        // Cache 생성
        jCacheManager.createCache("BIZ_MENU_LIST", configuration);
        jCacheManager.createCache("BIZDAY_LIST", configuration);
        jCacheManager.createCache("CMN_CD_GROUP", configuration);
        jCacheManager.createCache("CMN_CD", configuration);

        return new JCacheCacheManager(jCacheManager);
    }

}