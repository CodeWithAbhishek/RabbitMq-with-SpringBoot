package com.codewithabhishek;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.TimeUnit;

import com.codewithabhishek.consumer.Consumer;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@Component
public class SendMessage {

	@Value("${queue.name}")
	private String nameOfQueue;

	@Autowired
	private RabbitTemplate rt;

	@Autowired
	private Consumer consumer;

	@RequestMapping("/send")
	public String send() throws InterruptedException, JsonProcessingException {
		
		  MessageProperties properties = new MessageProperties();
		  properties.setHeader("HELLO", "HI");
		  properties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN); Message
		  message = new Message("CodeWithAbhishek We are sending".getBytes(),
		  properties); rt.sendAndReceive("", nameOfQueue, message);
		 
		
		//rt.convertAndSend(nameOfQueue, "Abhishek", "Hello from RabbitMQ!");
		consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);

		return "Message Has Been Sent";
	}

}
