package kr.fasol.obix.backend.common.cache;

import lombok.extern.slf4j.Slf4j;

import javax.cache.event.*;

/**
 * 설명 : 캐싱 모니터링
 *
 * @Class CustomCacheEntryListener.class
 * @since 2021/04/12
 * @author 정영탁
 * @version 1.0
 *
 * @Copyright (c) Finance All Solutions.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021/04/12        정영탁          최초생성
 */
@Slf4j
public class CustomCacheEntryListener implements CacheEntryCreatedListener<Object, Object>, CacheEntryUpdatedListener<Object, Object>, CacheEntryRemovedListener<Object, Object>, CacheEntryExpiredListener<Object, Object> {

    @Override
    public void onCreated(Iterable<CacheEntryEvent<?, ?>> iterable) throws CacheEntryListenerException {
        iterable.forEach(cacheEntryEvent -> {
            log.info("Cache Entry Created. Cache Event: {} / Key: {} / Value:{}",
                    cacheEntryEvent.getEventType(),
                    cacheEntryEvent.getKey(),
                    cacheEntryEvent.getValue());
        });
    }

    @Override
    public void onUpdated(Iterable<CacheEntryEvent<?, ?>> iterable) throws CacheEntryListenerException {
        iterable.forEach(cacheEntryEvent -> {
            log.info("Cache Entry Updated. Cache Event: {} / Key: {} / Old Value: {} / New Value:{}",
                    cacheEntryEvent.getEventType(),
                    cacheEntryEvent.getKey(),
                    cacheEntryEvent.getOldValue(),
                    cacheEntryEvent.getValue());
        });
    }

    @Override
    public void onRemoved(Iterable<CacheEntryEvent<?, ?>> iterable) throws CacheEntryListenerException {
        iterable.forEach(cacheEntryEvent -> {
            log.info("Cache Entry Removed. Cache Event: {} / Key: {} / Value:{}",
                    cacheEntryEvent.getEventType(),
                    cacheEntryEvent.getKey(),
                    cacheEntryEvent.getValue());
        });
    }

    @Override
    public void onExpired(Iterable<CacheEntryEvent<?, ?>> iterable) throws CacheEntryListenerException {
        iterable.forEach(cacheEntryEvent -> {
            log.info("Cache Entry Expired. Cache Event: {} / Key: {} / Value:{}",
                    cacheEntryEvent.getEventType(),
                    cacheEntryEvent.getKey(),
                    cacheEntryEvent.getValue());
        });
    }

}
