package xyz.zlatanov.subsbuddy.cli.setup;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.extern.slf4j.Slf4j;
import xyz.zlatanov.subsbuddy.core.connector.TranslationConnector;

@Slf4j
@Configuration
public class TestSetupConfig {

	@Bean
	@Primary
	public TranslationConnector capitalizingTranslationConnector() {
		log.info("FOO!");
		return new CapitalizingTranslationConnector();
	}
}
