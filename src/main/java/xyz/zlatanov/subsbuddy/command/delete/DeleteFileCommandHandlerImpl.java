package xyz.zlatanov.subsbuddy.command.delete;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;
import xyz.zlatanov.subsbuddy.repository.TranslationRepository;

@Service
@AllArgsConstructor
public class DeleteFileCommandHandlerImpl implements DeleteFileCommandHandler {

	private MovieSubtitleRepository	movieSubtitleRepository;
	private TranslationRepository	translationRepository;

	@Override
	@Transactional
	public void execute(DeleteFileCommand command) {
		movieSubtitleRepository.deleteById(command.id());
		translationRepository.deleteAllBySourceId(command.id());
	}
}
