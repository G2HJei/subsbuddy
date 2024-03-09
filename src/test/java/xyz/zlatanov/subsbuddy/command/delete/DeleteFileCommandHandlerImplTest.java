package xyz.zlatanov.subsbuddy.command.delete;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

class DeleteFileCommandHandlerImplTest {

	MovieSubtitleRepository	movieSubtitleRepository	= mock(MovieSubtitleRepository.class);
	DeleteFileCommandHandler			handler					= new DeleteFileCommandHandlerImpl(movieSubtitleRepository);

	@Test
	void deleteFileHandler_deleteFile_returns() {
		doNothing().when(movieSubtitleRepository).deleteById("test");
		assertDoesNotThrow(() -> handler.execute(new DeleteFileCommand().id("test")));
	}

}