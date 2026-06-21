package com.wildace.poker.jacksorbetter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class VideoPokerApplication {
	public static void main(String[] args) {
		SpringApplication.run(VideoPokerApplication.class, args);
	}


	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		System.out.println("Application 1.6.21 is ready. Initializing game...");
	}

}
