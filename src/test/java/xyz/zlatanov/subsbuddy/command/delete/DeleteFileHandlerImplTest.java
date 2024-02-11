package xyz.zlatanov.subsbuddy.command.delete;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

class DeleteFileHandlerImplTest {

	MovieSubtitleRepository	movieSubtitleRepository	= mock(MovieSubtitleRepository.class);
	DeleteFileHandler		handler					= new DeleteFileHandlerImpl(movieSubtitleRepository);

	@Test
	void deleteFileHandler_deleteFile_returns() {
		doNothing().when(movieSubtitleRepository).deleteById("test");
		assertDoesNotThrow(() -> handler.deleteFile(new DeleteFileCommand().id("test")));
	}

}