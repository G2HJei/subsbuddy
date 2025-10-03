package xyz.zlatanov.subsbuddy.cli;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import xyz.zlatanov.subsbuddy.core.client.SubsBuddyClient;
import xyz.zlatanov.subsbuddy.core.connector.TranslationConnector;
import xyz.zlatanov.subsbuddy.core.connector.deepl.DeepLTranslationConnector;

@Slf4j
@Configuration
public class SpringConfig {

	@Bean
	@ConditionalOnProperty("DEEPL_API_KEY")
	public TranslationConnector translationConnector(@Value("${DEEPL_API_KEY}") String... apiKeys) {
		return new DeepLTranslationConnector(Arrays.asList(apiKeys));
	}

	@Bean
	public SubsBuddyClient subsBuddyClient(TranslationConnector translationConnector) {
		return new SubsBuddyClient(translationConnector);
	}
}
