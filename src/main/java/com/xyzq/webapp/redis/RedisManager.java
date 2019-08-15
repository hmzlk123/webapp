package com.xyzq.webapp.redis;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.util.CollectionUtils;

/**
 * Package: com.xyzq.webapp.redis
 * Description： redis管理
 * Author: linkan
 * Date: Created in 2019/8/1 14:55
 * Company: 兴业证券
 * Copyright: Copyright (c) 2019
 * Version: 0.0.1
 */
public class RedisManager {
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
	/**
	 * Title: expire
	 * Description: 指定缓存失效时间
	 * @param key 键
	 * @param time  时间(秒)
	 */
    public void expire(String key,long time){
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * Title: hasKey
     * Description: 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * Title: del
     * Description: 删除缓存
     * @param key 可以传一个值 或多个
     */
    public void del(String ... key){
        if(key!=null&&key.length>0){
            if(key.length==1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * Title: del
     * Description: 批量删除key
     * @param keys 键
     */
    public void del(Collection<String> keys){
        redisTemplate.delete(keys);
    }


    /**
     * Title: get
     * Description: 普通缓存获取
     * @param key 键
     * @return Object
     */
    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 
     * Title: set
     * Description: 普通缓存放入
     * @param key  键
     * @param  value  值
     */
    public void set(String key,Object value) {
        redisTemplate.opsForValue().set(key, value);
    }


    /**
     * Title: set
     * Description: 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     */
    public void set(String key,Object value,long time){
        if(time>0){
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        }else{
            set(key, value);
        }
    }


    /**
     * Title: scan
     * Description: 使用scan命令 查询某些前缀的key
     * @param key 键
     * @return Set<String>
     */
    public Set<String> scan(String key){
        return this.redisTemplate.execute((RedisCallback<Set<String>>) connection -> {

            Set<String> binaryKeys = new HashSet<>();

            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(key).count(1000).build());
            while (cursor.hasNext()) {
                binaryKeys.add(new String(cursor.next()));
            }
            return binaryKeys;
        });
    }

    /**
     * Title: scanSize
     * Description:使用scan命令 查询某些前缀的key 有多少个
     * @param key 键
     * @return Long
     */
    public Long scanSize(String key){
        return this.redisTemplate.execute((RedisCallback<Long>) connection -> {
            long count = 0L;
            Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(key).count(1000).build());
            while (cursor.hasNext()) {
                cursor.next();
                count++;
            }
            return count;
        });
    }

}
