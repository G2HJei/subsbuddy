package xyz.zlatanov.subsbuddy.cli.setup;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import xyz.zlatanov.subsbuddy.core.connector.TranslationConnector;

@Configuration
public class TestSetupConfig {

	@Bean
	@Primary
	public TranslationConnector capitalizingTranslationConnector() {
		return new CapitalizingTranslationConnector();
	}
}
