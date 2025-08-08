package xyz.zlatanov.subsbuddy.connector.deepl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.deepl.api.DeepLClient;
import com.deepl.api.TextTranslationOptions;
import com.deepl.api.Usage;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import xyz.zlatanov.subsbuddy.connector.TranslationConnector;
import xyz.zlatanov.subsbuddy.domain.Language;

@Service
@Slf4j
public class DeepLTranslationConnector implements TranslationConnector {

	@Value("${DEEPL_API_KEY}")
	private String		apiKey;
	private DeepLClient	client;

	@PostConstruct
	public void init() {
		client = new DeepLClient(apiKey);
		logUsage();
	}

	@Override
	@SneakyThrows
	public String translate(String text, Language from, Language to, String context) {
		val options = new TextTranslationOptions().setContext(context);
		log.debug("Context    : {}", context.replace("\n", " "));
		log.debug("Translating: {}", text);
		val translation = client.translateText(text, from.name().toLowerCase(), to.name().toLowerCase(), options);
		val translatedText = translation.getText();
		log.debug("          -> {}\n", translatedText);
		return translatedText;
	}

	@SneakyThrows
	private void logUsage() {
		val usageLog = Optional.ofNullable(client.getUsage())
				.map(Usage::getCharacter)
				.map(u -> u.getCount() + "/" + u.getLimit())
				.orElse("?/?");
		log.info("DeepL usage: {} characters limit", usageLog);
	}
}
