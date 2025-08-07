package xyz.zlatanov.subsbuddy.command.upload;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import xyz.zlatanov.subsbuddy.command.translate.TranslateFileCommandHandler;
import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.exception.AlreadyUploadedException;
import xyz.zlatanov.subsbuddy.exception.NotSupportedFileTypeException;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

public class FileUploadHandlerImplTest {

	MovieSubtitleRepository		movieSubtitleRepository	= mock(MovieSubtitleRepository.class);
	TranslateFileCommandHandler	translateFileHandler	= mock(TranslateFileCommandHandler.class);
	UploadFileCommandHandler	handler					= new UploadFileCommandHandlerImpl(movieSubtitleRepository, translateFileHandler);

	@BeforeEach
	void setup() {
		doNothing().when(translateFileHandler).execute(any());
	}

	@Test
	void execute_validFile_returns() {
		when(movieSubtitleRepository.save(any())).thenReturn(new MovieSubtitle());
		assertDoesNotThrow(() -> handler.execute(new UploadFileCommand().filename("test.srt").content("test")));
	}

	@Test
	void execute_nonSrtFile_throws() {
		assertThrows(NotSupportedFileTypeException.class, () -> handler.execute(new UploadFileCommand().filename("test.zip")));
	}

	@Test
	void execute_alreadyUploaded_throws() {
		when(movieSubtitleRepository.findOneByFilename("alreadyUploaded.srt")).thenReturn(new MovieSubtitle());
		assertThrows(AlreadyUploadedException.class,
				() -> handler.execute(new UploadFileCommand().filename("alreadyUploaded.srt")));
	}
}
