package xyz.zlatanov.subsbuddy.connector.translation.google.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.AllArgsConstructor;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
@AllArgsConstructor
public class GoogleTranslateRestClientConfig {

	@Bean
	GoogleTranslateRestClient getTranslateRestClient() {
		return Feign.builder()
				.client(new OkHttpClient())
				.logger(new Slf4jLogger(GoogleTranslateRestClient.class))
				.logLevel(Logger.Level.FULL)
				.retryer(new Retryer.Default(100L, SECONDS.toMillis(3L), 3))
				.target(GoogleTranslateRestClient.class, "https://script.google.com");
	}

}
