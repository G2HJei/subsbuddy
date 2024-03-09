package xyz.zlatanov.subsbuddy.query.translatetext;

import org.springframework.stereotype.Service;

@Service
public class TranslateTextQueryHandlerImpl implements TranslateTextQueryHandler {

	@Override
	public TranslateTextProjection execute(TranslateTextQuery query) {
		return new TranslateTextProjection();
	}
}
