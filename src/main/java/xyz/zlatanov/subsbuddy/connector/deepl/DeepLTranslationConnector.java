package xyz.zlatanov.subsbuddy.connector.deepl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.deepl.api.DeepLClient;
import com.deepl.api.DeepLException;
import com.deepl.api.TextTranslationOptions;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import xyz.zlatanov.subsbuddy.connector.TranslationConnector;
import xyz.zlatanov.subsbuddy.domain.Language;
import xyz.zlatanov.subsbuddy.exception.SubsBuddyException;

@Service
@Slf4j
public class DeepLTranslationConnector implements TranslationConnector {

	@Value("${DEEPL_API_KEY}")
	private String		apiKey;

	private DeepLClient	client;

	@PostConstruct
	public void init() {
		client = new DeepLClient(apiKey);
		usagePercent();
	}

	@Override
	@SneakyThrows
	public long usagePercent() {
		val charUsage = client.getUsage().getCharacter();
		assert charUsage != null;
		val percentUsed = (charUsage.getCount() * 100) / charUsage.getLimit();
		val usageLog = charUsage.getCount() + "/" + charUsage.getLimit();
		log.info("DeepL usage: {}% ({} characters used)", percentUsed, usageLog);
		return percentUsed;
	}

	@Override
	public String translate(String text, Language from, Language to, String context) {
		log.debug("Context    : {}", context.replace("\n", " "));
		log.debug("Translating: {}", text);
		val options = new TextTranslationOptions().setContext(context);
		val translatedText = attemptTranslation(text, from, to, options, 0);
		log.debug("          -> {}\n", translatedText);
		return translatedText;
	}

	@SneakyThrows
	private String attemptTranslation(String text, Language from, Language to, TextTranslationOptions options, long retryCount) {
		delayRetry(retryCount);
		try {
			return client.translateText(text, from.name(), to.name(), options).getText();
		} catch (DeepLException e) {
			return attemptTranslation(text, from, to, options, retryCount + 1);
		}
	}

	@SneakyThrows
	private void delayRetry(long retryCount) {
		if (retryCount > 3) {
			throw new SubsBuddyException("Translation retry limit exceeded");
		} else {
			Thread.sleep(retryCount * 2_000);
		}
	}
}
