package xyz.zlatanov.subsbuddy.query.sentencedetection;

import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import lombok.val;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

@Service
public class SentenceDetectionQueryHandlerImpl implements SentenceDetectionQueryHandler {

	private final SentenceDetector sentenceDetector;

	@SneakyThrows
	public SentenceDetectionQueryHandlerImpl() {
		InputStream inputStream = getClass().getResourceAsStream("/language-models/opennlp-en.bin");
        assert inputStream != null;
        val model = new SentenceModel(inputStream);
		sentenceDetector = new SentenceDetectorME(model);
	}

	@Override
	public SentenceDetectionProjection execute(SentenceDetectionQuery query) {
		val spacePaddedText = addSpacesAfterPunctuation(query.text());
		return new SentenceDetectionProjection().sentenceList(List.of(sentenceDetector.sentDetect(spacePaddedText)));
	}

	public static String addSpacesAfterPunctuation(String input) {
		return input.replaceAll("([.!?])([^\\s])", "$1 $2");
	}
}
