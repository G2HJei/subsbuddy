package xyz.zlatanov.subsbuddy.query.assemblesubscontent;

import org.springframework.stereotype.Service;

@Service
public class AssembleSubsContentQueryHandlerImpl implements AssembleSubsContentQueryHandler {

	@Override
	public AssembleSubsContentQueryProjection execute(AssembleSubsContentQuery query) {
		return new AssembleSubsContentQueryProjection();
	}
}
