package com.codewithabhishek.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

import com.codewithabhishek.consumer.Consumer;

@Configuration
public class RabbitMqConfig {

	@Value("${queue.name}")
	private String nameOfQueue;

	@Value("${topic.name}")
	private String topicName;

	@Bean
	public Queue queue() {
		return new Queue(nameOfQueue, false);
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(topicName);

	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("binding");
	}

	
	
	
	
	@Bean
	SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {

		SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
		listenerContainer.setConnectionFactory(connectionFactory);
		listenerContainer.setQueueNames(nameOfQueue);
		listenerContainer.setMessageListener(listenerAdapter);
		listenerContainer.setErrorHandler(new ErrorHandler() {
			@Override
			public void handleError(Throwable t) {
				System.err.println("An error has occurred in your Listener");
			}
		});

		return listenerContainer;

	}

	@Bean
	MessageListenerAdapter listenerAdapter(Consumer consumer) {

		return new MessageListenerAdapter(consumer, "listen");
	}

}
