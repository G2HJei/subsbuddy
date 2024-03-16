package xyz.zlatanov.subsbuddy.query.download;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import xyz.zlatanov.subsbuddy.exception.SubtitleNotFoundException;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

@Service
@AllArgsConstructor
public class DownloadFileQueryHandlerImpl implements DownloadFileQueryHandler {

	private MovieSubtitleRepository repository;

	@Override
	public DownloadFileProjection execute(String id) {
		return repository.findById(id)
				.map(f -> new DownloadFileProjection()
						.filename(f.filename())
						.content(f.subtitleData()))
				.orElseThrow(SubtitleNotFoundException::new);
	}
}
