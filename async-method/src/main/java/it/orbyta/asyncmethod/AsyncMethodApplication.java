package it.orbyta.asyncmethod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AsyncMethodApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsyncMethodApplication.class, args);
	}

}
