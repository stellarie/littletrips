package com.littletrips.tripsim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.webmvc.autoconfigure.error.ErrorMvcAutoConfiguration;

@SpringBootApplication
public class TripsimApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripsimApplication.class, args);
	}

}
