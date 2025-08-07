package xyz.zlatanov.subsbuddy.query.sentencedetect;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import lombok.val;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

@Service
public class SentenceDetectQueryHandlerImpl implements SentenceDetectQueryHandler {

	private final SentenceDetector sentenceDetector;

	@SneakyThrows
	public SentenceDetectQueryHandlerImpl() {
		val inputStream = getClass().getResourceAsStream("/language-models/opennlp-en.bin");
		assert inputStream != null;
		val model = new SentenceModel(inputStream);
		sentenceDetector = new SentenceDetectorME(model);
	}

	@Override
	public SentenceDetectProjection execute(SentenceDetectQuery query) {
		val spacePaddedText = addSpacesAfterPunctuation(query.text());
		return new SentenceDetectProjection().sentenceList(List.of(sentenceDetector.sentDetect(spacePaddedText)));
	}

	private static String addSpacesAfterPunctuation(String input) {
		return input.replaceAll("([.!?])([^\\s])", "$1 $2");
	}
}
