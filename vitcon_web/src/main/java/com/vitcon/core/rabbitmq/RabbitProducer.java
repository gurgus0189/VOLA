package com.vitcon.core.rabbitmq;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitProducer {
	
	@Autowired
	private ConnectionFactory connectionFactory;
	
	private String exchange = "amq.topic";
	
	public void sendMessage(String routingKey, String message) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setExchange(this.exchange);
	    rabbitTemplate.setRoutingKey(routingKey);
	    
	    //System.err.println("routingkey" + routingKey + ", message=" + message);

	    rabbitTemplate.convertAndSend(message);
	}

}