package xyz.zlatanov.subsbuddy.command.delete;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

@Service
@AllArgsConstructor
public class DeleteFileHandlerImpl implements DeleteFileHandler {

	private MovieSubtitleRepository movieSubtitleRepository;

	@Override
	@Transactional
	public void deleteFile(DeleteFileCommand command) {
		movieSubtitleRepository.deleteById(command.id());
	}
}
