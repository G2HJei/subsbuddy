package xyz.zlatanov.subsbuddy.query.availablesubs;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

@Service
@AllArgsConstructor
public class AvailableSubsQueryHandlerImpl implements AvailableSubsQueryHandler {

	private MovieSubtitleRepository repository;

	@Override
	public AvailableSubsProjection list(AvailableSubsQuery query) {
		return new AvailableSubsProjection(
				repository.findByOwner(query.owner()).stream()
						.map(m -> new SubDetails(m.id(), m.filename(), m.language().toString()))
						.toList());
	}
}