package xyz.zlatanov.subsbuddy.query.formatsubscontent;

import org.springframework.stereotype.Service;

@Service
public class FormatSubsQueryHandlerImpl implements FormatSubsQueryHandler {

	@Override
	public FormatSubsQueryProjection execute(FormatSubsContentQuery query) {
		return new FormatSubsQueryProjection();
	}
}
