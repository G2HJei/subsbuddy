package xyz.zlatanov.subsbuddy.cli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan
@SpringBootApplication
public class SubsBuddyCliApp {

	public static void main(String[] args) {
		SpringApplication.run(SubsBuddyCliApp.class, args);
	}

}
