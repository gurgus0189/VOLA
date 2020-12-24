package com.vitcon.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {
 
	@Value("${redis.message.host}")
	private String redisHost;
	
	@Value("${redis.message.port}")
	private int redisPort;
	
	@Value("${redis.message.password}")
	private String redisPassword;	
		
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
    	RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration();
    	conf.setHostName(redisHost);
    	conf.setPort(redisPort);
    	conf.setPassword(RedisPassword.of(redisPassword));    	
    	
    	LettuceConnectionFactory factory = new LettuceConnectionFactory(conf);    	
    	return factory;
    }        

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
    	RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();        
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

}