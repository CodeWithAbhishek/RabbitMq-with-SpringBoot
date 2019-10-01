package com.codewithabhishek.consumer;

import java.util.concurrent.CountDownLatch;

import org.springframework.stereotype.Component;

@Component
public class Consumer {

	private CountDownLatch latch = new CountDownLatch(5);

	public void listen(String message) {

		System.out.println("Got your Message" + "  ====   " + message);

		latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}
	
}
