package xyz.zlatanov.subsbuddy.connector.translation.google;

import static xyz.zlatanov.subsbuddy.domain.Language.BG;
import static xyz.zlatanov.subsbuddy.domain.Language.EN;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import xyz.zlatanov.subsbuddy.connector.translation.TranslationConnector;
import xyz.zlatanov.subsbuddy.connector.translation.google.client.GoogleTranslateRestClient;
import xyz.zlatanov.subsbuddy.domain.Language;

@Service
public class GoogleTranslateTranslationConnector implements TranslationConnector {

	private final GoogleTranslateRestClient	restClient;
	private final String					deploymentId;

	public GoogleTranslateTranslationConnector(GoogleTranslateRestClient restClient,
			@Value("${GOOGLE_MACROS_TRANSLATE_DEPLOYMENT_ID}") String deploymentId) {
		this.restClient = restClient;
		this.deploymentId = deploymentId;
	}

	@SneakyThrows
	@Override
	public String translate(String text, Language from, Language to) {
		if (from != EN || to != BG) {
			throw new UnsupportedOperationException("Only EN->BG translation is supported.");
		}
		return restClient.translate(deploymentId, text, from.name().toLowerCase(), to.name().toLowerCase());
	}
}
