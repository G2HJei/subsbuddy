package xyz.zlatanov.subsbuddy.command.upload;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.val;
import xyz.zlatanov.subsbuddy.command.translate.TranslateFileCommandHandler;
import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.exception.NotSupportedFileTypeException;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

@ExtendWith(MockitoExtension.class)
class FileUploadHandlerImplTest {

	@Mock
	MovieSubtitleRepository			movieSubtitleRepository;
	@Mock
	TranslateFileCommandHandler		translateFileHandler;
	@InjectMocks
	UploadFileCommandHandlerImpl	handler;

	@Test
	void execute_validFile_returns() {
		when(movieSubtitleRepository.save(any())).thenReturn(new MovieSubtitle());
		assertDoesNotThrow(() -> handler.execute(new UploadFileCommand().filename("test.srt").content("test")));
	}

	@Test
	void execute_nonSrtFile_throws() {
		val uploadFileCommand = new UploadFileCommand().filename("test.zip");
		assertThrows(NotSupportedFileTypeException.class, () -> handler.execute(uploadFileCommand));
	}
}
