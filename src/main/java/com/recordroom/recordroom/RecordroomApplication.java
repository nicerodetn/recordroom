package com.recordroom.recordroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RecordroomApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecordroomApplication.class, args);
	}

}
