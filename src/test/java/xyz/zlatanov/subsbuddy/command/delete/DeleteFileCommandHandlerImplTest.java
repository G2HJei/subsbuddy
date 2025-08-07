package xyz.zlatanov.subsbuddy.command.delete;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;
import xyz.zlatanov.subsbuddy.repository.TranslationRepository;

class DeleteFileCommandHandlerImplTest {

	MovieSubtitleRepository		movieSubtitleRepository	= mock(MovieSubtitleRepository.class);
	TranslationRepository		translationRepository	= mock(TranslationRepository.class);
	DeleteFileCommandHandler	handler					= new DeleteFileCommandHandlerImpl(movieSubtitleRepository, translationRepository);

	UUID						subtitleId				= UUID.randomUUID();

	@Test
	void execute_deleteFile_returns() {
		doNothing().when(movieSubtitleRepository).deleteById(subtitleId);
		doNothing().when(translationRepository).deleteAllBySourceSubtitleId(subtitleId);
		assertDoesNotThrow(() -> handler.execute(new DeleteFileCommand().id(subtitleId)));
	}

}