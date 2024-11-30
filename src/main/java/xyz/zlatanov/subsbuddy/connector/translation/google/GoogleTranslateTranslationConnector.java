package xyz.zlatanov.subsbuddy.connector.translation.google;

import static xyz.zlatanov.subsbuddy.domain.Language.BG;
import static xyz.zlatanov.subsbuddy.domain.Language.EN;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import xyz.zlatanov.subsbuddy.connector.translation.TranslationConnector;
import xyz.zlatanov.subsbuddy.connector.translation.google.client.GoogleTranslateRestClient;
import xyz.zlatanov.subsbuddy.domain.Language;

@Service
@Slf4j
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
	public String translate(String original, Language from, Language to) {
		if (from != EN || to != BG) {
			throw new UnsupportedOperationException("Only EN->BG translation is supported.");
		}
		try {
			val translated = restClient.translate(deploymentId, original, from.name().toLowerCase(), to.name().toLowerCase());
			return translationIsTooLong(original, translated) ? original : translated;
		} catch (Exception e) {
			log.error("Translation failed: {}", ExceptionUtils.getStackTrace(e));
			return original;
		}
	}

	private boolean translationIsTooLong(String text, String translatedText) {
		val translationDeviationFactor = 4;
		return translatedText.length() >= text.length() * translationDeviationFactor;
	}
}
