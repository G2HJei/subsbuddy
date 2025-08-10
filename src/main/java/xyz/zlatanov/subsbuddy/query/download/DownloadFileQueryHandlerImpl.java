package xyz.zlatanov.subsbuddy.query.download;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import xyz.zlatanov.subsbuddy.exception.SubtitleNotFoundException;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

@Service
@AllArgsConstructor
public class DownloadFileQueryHandlerImpl implements DownloadFileQueryHandler {

	private MovieSubtitleRepository repository;

	@Override
	public DownloadFileProjection execute(UUID id) {
		return repository.findById(id)
				.map(f -> new DownloadFileProjection()
						.filename(f.name())
						.content(f.subtitleData()))
				.orElseThrow(SubtitleNotFoundException::new);
	}
}
