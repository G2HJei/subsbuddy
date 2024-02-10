package xyz.zlatanov.subsbuddy.command.upload;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static xyz.zlatanov.subsbuddy.domain.Language.ENGLISH;

import org.junit.jupiter.api.Test;

import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.exception.NotSupportedFileType;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

public class FileUploadHandlerTest {

	MovieSubtitleRepository	movieSubtitleRepository	= mock(MovieSubtitleRepository.class);
	UploadFileHandler		handler					= new UploadFileHandlerImpl(movieSubtitleRepository);

	@Test
	void uploadFile_validFile_returns() {
		when(movieSubtitleRepository.insert((MovieSubtitle) any())).thenReturn(new MovieSubtitle("mock", ENGLISH, "mock"));
		assertDoesNotThrow(() -> handler.uploadFile(new FileUploadCommand().filename("test.srt")));
	}

	@Test
	void uploadFile_nonSrtFile_throws() {
		assertThrows(NotSupportedFileType.class, () -> handler.uploadFile(new FileUploadCommand().filename("test.zip")));
	}
}
