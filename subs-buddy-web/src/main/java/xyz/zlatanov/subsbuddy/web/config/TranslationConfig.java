package xyz.zlatanov.subsbuddy.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import xyz.zlatanov.subsbuddy.core.SubsBuddyClient;

@Configuration
public class TranslationConfig {

	@Bean
	SubsBuddyClient subsBuddyClient(@Value("${DEEPL_API_KEY}") String... apiKeys) {
		return SubsBuddyClient.deepL(apiKeys);
	}
}
