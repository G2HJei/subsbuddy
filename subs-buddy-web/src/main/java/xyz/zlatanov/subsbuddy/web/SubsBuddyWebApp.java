package xyz.zlatanov.subsbuddy.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SubsBuddyWebApp {

	public static void main(String[] args) {
		SpringApplication.run(SubsBuddyWebApp.class, args);
	}

}
