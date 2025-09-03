package xyz.zlatanov.subsbuddy.web.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.val;
import xyz.zlatanov.subsbuddy.core.client.SubsBuddyClient;
import xyz.zlatanov.subsbuddy.core.connector.deepl.DeepLTranslationConnector;

@Configuration
public class TranslationConfig {

	@Bean
	SubsBuddyClient subsBuddyClient(@Value("${DEEPL_API_KEY}") String... apiKeys) {
		val translationConnector = new DeepLTranslationConnector(Arrays.asList(apiKeys));
		return new SubsBuddyClient(translationConnector);
	}
}
