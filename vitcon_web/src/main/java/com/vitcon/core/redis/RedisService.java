package com.vitcon.core.redis;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisService {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisService.class);
        
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    /**
     * redis 의 key-value store 에 key value 를 저장
     * @param key key
     * @param value value
     */
    public void setValue(String key, String value) {
    	try {
    		redisTemplate.opsForValue().set(key, value);
    	} catch(Exception e) {
    	}
    }
    
    /**
     * redis 의 key-value store 에 key value 를 저장하는데, 
     * timeout 을 두어 특정 시간이 지나면 key 를 삭제
     * @param key key
     * @param value value
     * @param timeout 타임아웃 시간(초)
     */
    public void setValue(String key, String value, long timeout) {
    	try {
    		logger.debug("key set ttl=" + key + ",value=" + value + ",timeout=" + timeout);
    		redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    	} catch(Exception e) {    		
    	}
    }
    
    /**
     * redis 의 key-value store 에서 key 에 해당하는 값을 반환
     * @param key key
     * @return 값이 있으면 String 반환, 없으면 null 반환
     */
    public String getValue(String key) {
    	String ret = null;
    	
    	try {
    		ret = redisTemplate.opsForValue().get(key);
    	} catch(Exception e) {    		
    	}
    	
    	return ret;
    }
    
    public List<String> multiGet(Set<String> keys) {
    	List<String> ret = redisTemplate.opsForValue().multiGet(keys);
    	return ret;
    }
    
    // 공지사항 redis
    public List<String> noticeGet(Set<String> keys) {
    	List<String> ret = redisTemplate.opsForValue().multiGet(keys);
    	return ret;
    }
    
    // 광고 팝업 redis
    public List<String> popupGet(Set<String> keys) {
    	List<String> ret = redisTemplate.opsForValue().multiGet(keys);
    	return ret;
    }
    
    public boolean deleteKey(String key) {
    	boolean ret = false;
    	
    	try {
    		ret = redisTemplate.delete(key);
    	} catch(Exception e) {    		
    	}
    	return ret;
    }
}