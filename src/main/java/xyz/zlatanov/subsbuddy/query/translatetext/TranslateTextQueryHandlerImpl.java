package xyz.zlatanov.subsbuddy.query.translatetext;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import xyz.zlatanov.subsbuddy.connector.TranslationConnector;
import xyz.zlatanov.subsbuddy.query.translatetext.helper.EntriesGrouper;
import xyz.zlatanov.subsbuddy.query.translatetext.helper.TranslationHelper;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslateTextQueryHandlerImpl implements TranslateTextQueryHandler {

	private final TranslationConnector translationConnector;

	@Override
	public TranslateTextProjection execute(TranslateTextQuery query) {
		val groupedEntries = EntriesGrouper.create(query.linesList()).group();
		val translatedLines = TranslationHelper.create(groupedEntries, translationConnector).translate();
		return new TranslateTextProjection().linesList(translatedLines);
	}

}
