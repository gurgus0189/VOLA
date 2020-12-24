/**
 * 
 */
package com.vitcon.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfiguration {
	
	@Value("${mq.host}")
	private String host;
	
	@Value("${mq.userid}")
	private String userid;
	
	@Value("${mq.password}")
	private String password;
	
	@Value("${mq.port}")
	private int port; 
	
	@Value("${mq.cachesize}")
	private int cachesize;

	private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfiguration.class);

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
		connectionFactory.setUsername(userid);
		connectionFactory.setPassword(password);
		connectionFactory.setPort(port);
		//connectionFactory.setConnectionCacheSize(25);
		connectionFactory.setChannelCacheSize(cachesize);
		
		logger.info(String.format("server connect : %s:%d, userid=%s, password=%s", host,
				port, userid, password));
		
		return connectionFactory;
	}

}