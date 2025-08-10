package xyz.zlatanov.subsbuddy.command.delete;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

@Service
@AllArgsConstructor
public class DeleteSubtitleCommandHandlerImpl implements DeleteSubtitleCommandHandler {

	private MovieSubtitleRepository movieSubtitleRepository;

	@Override
	@Transactional
	public void execute(UUID subId) {
		movieSubtitleRepository.deleteAllByTranslatedFromSubtitleId(subId);
		movieSubtitleRepository.deleteById(subId);
	}
}
