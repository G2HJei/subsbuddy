package xyz.zlatanov.subsbuddy.query.splitlines;

import org.springframework.stereotype.Service;

@Service
public class SplitLinesQueryHandlerImpl implements SplitLinesQueryHandler {

	@Override
	public SplitLinesProjection execute(SplitLinesQuery query) {
		return new SplitLinesProjection();
	}
}
