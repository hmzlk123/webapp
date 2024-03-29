package com.xyzq.webapp.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.xyzq.webapp.redis.RedisManager;

/**
 * Package: com.xyzq.webapp.shiro
 * Description：redis缓存
 * Author: linkan
 * Date: Created in 2019/8/1 14:55
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
public class RedisCache<K, V> implements Cache<K, V> {
	
	private static Logger logger = LoggerFactory.getLogger(RedisCache.class);
	private RedisManager redisManager;
	private String keyPrefix = "";
	private int expire = 0;
	private String principalIdFieldName = RedisCacheManager.DEFAULT_PRINCIPAL_ID_FIELD_NAME;


	/**
     * Title: RedisCache
     * Description: 构造方法
	 * @param redisManager  redis管理类
	 * @param prefix 前缀
	 * @param expire 过期失效
	 * @param principalIdFieldName  凭证字段名
	 */
    RedisCache(RedisManager redisManager, String prefix, int expire, String principalIdFieldName) {
        if (redisManager == null) {
            throw new IllegalArgumentException("redisManager cannot be null.");
        }
        this.redisManager = redisManager;
        if (prefix != null && !"".equals(prefix)) {
            this.keyPrefix = prefix;
        }
        if (expire != -1) {
            this.expire = expire;
        }
        if (principalIdFieldName != null && !"".equals(principalIdFieldName)) {
            this.principalIdFieldName = principalIdFieldName;
        }
    }

	@Override
    public V get(K key) throws CacheException {
        logger.debug("get key [{}]",key);

        if (key == null) {
            return null;
        }

        try {
            String redisCacheKey = getRedisCacheKey(key);
            Object rawValue = redisManager.get(redisCacheKey);
            if (rawValue == null) {
                return null;
            }
            return (V) rawValue;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public V put(K key, V value) throws CacheException {
        logger.debug("put key [{}]",key);
        if (key == null) {
            logger.warn("Saving a null key is meaningless, return value directly without call Redis.");
            return value;
        }
        try {
            String redisCacheKey = getRedisCacheKey(key);
            redisManager.set(redisCacheKey, value, expire);
            return value;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public V remove(K key) throws CacheException {
        logger.debug("remove key [{}]",key);
        if (key == null) {
            return null;
        }
        try {
            String redisCacheKey = getRedisCacheKey(key);
            Object rawValue = redisManager.get(redisCacheKey);
            V previous = (V) rawValue;
            redisManager.del(redisCacheKey);
            return previous;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    private String getRedisCacheKey(K key) {
        if (key == null) {
            return null;
        }
        return this.keyPrefix + getStringRedisKey(key);
    }

    private String getStringRedisKey(K key) {
        return key.toString();
    }

    @Override
    public void clear() throws CacheException {
        logger.debug("clear cache");
        Set<String> keys = null;
        try {
            keys = redisManager.scan(this.keyPrefix + "*");
        } catch (Exception e) {
            logger.error("get keys error", e);
        }
        if (keys == null || keys.size() == 0) {
            return;
        }
        for (String key: keys) {
            redisManager.del(key);
        }
    }

    @Override
    public int size() {
        Long longSize = 0L;
        try {
            longSize = redisManager.scanSize(this.keyPrefix + "*");
        } catch (Exception e) {
            logger.error("get keys error", e);
        }
        return longSize.intValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        Set<String> keys;
        try {
            keys = redisManager.scan(this.keyPrefix + "*");
        } catch (Exception e) {
            logger.error("get keys error", e);
            return Collections.emptySet();
        }

        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }

        Set<K> convertedKeys = new HashSet<>();
        for (String key:keys) {
            try {
                convertedKeys.add((K) key);
            } catch (Exception e) {
                logger.error("deserialize keys error", e);
            }
        }
        return convertedKeys;
    }

    @SuppressWarnings("unchecked")
	@Override
    public Collection<V> values() {
        Set<String> keys;
        try {
            keys = redisManager.scan(this.keyPrefix + "*");
        } catch (Exception e) {
            logger.error("get values error", e);
            return Collections.emptySet();
        }

        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }

        List<V> values = new ArrayList<>(keys.size());
        for (String key : keys) {
            V value = null;
            try {
                value = (V) redisManager.get(key);
            } catch (Exception e) {
                logger.error("deserialize values= error", e);
            }
            if (value != null) {
                values.add(value);
            }
        }
        return Collections.unmodifiableList(values);
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getPrincipalIdFieldName() {
        return principalIdFieldName;
    }

    public void setPrincipalIdFieldName(String principalIdFieldName) {
        this.principalIdFieldName = principalIdFieldName;
    }

}
