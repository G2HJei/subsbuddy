package xyz.zlatanov.subsbuddy.command.delete;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

@Service
@AllArgsConstructor
public class DeleteFileCommandHandlerImpl implements DeleteFileCommandHandler {

	private MovieSubtitleRepository movieSubtitleRepository;

	@Override
	@Transactional
	public void execute(DeleteFileCommand command) {
		movieSubtitleRepository.deleteAllByTranslatedFromSubtitleId(command.id());
		movieSubtitleRepository.deleteById(command.id());
	}
}
