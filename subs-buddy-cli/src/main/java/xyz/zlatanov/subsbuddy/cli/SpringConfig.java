package xyz.zlatanov.subsbuddy.cli;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import xyz.zlatanov.subsbuddy.core.client.SubsBuddyClient;

@Configuration
public class SpringConfig {

	@Bean
	SubsBuddyClient subsBuddyClient(@Value("${DEEPL_API_KEY}") String... apiKeys) {
		return SubsBuddyClient.deepL(apiKeys);
	}
}
