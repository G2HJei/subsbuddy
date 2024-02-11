package xyz.zlatanov.subsbuddy.command.upload;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.exception.AlreadyUploaded;
import xyz.zlatanov.subsbuddy.exception.NotSupportedFileType;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

public class FileUploadHandlerImplTest {

	MovieSubtitleRepository	movieSubtitleRepository	= mock(MovieSubtitleRepository.class);
	UploadFileHandler		handler					= new UploadFileHandlerImpl(movieSubtitleRepository);

	@Test
	void uploadFile_validFile_returns() {
		when(movieSubtitleRepository.insert((MovieSubtitle) any())).thenReturn(new MovieSubtitle());
		assertDoesNotThrow(() -> handler.uploadFile(new FileUploadCommand().filename("test.srt").content("test")));
	}

	@Test
	void uploadFile_nonSrtFile_throws() {
		assertThrows(NotSupportedFileType.class, () -> handler.uploadFile(new FileUploadCommand().filename("test.zip")));
	}

	@Test
	void uploadFile_alreadyUploaded_throws(){
		when(movieSubtitleRepository.findOneByOwnerAndFilename("me", "alreadyUploaded.srt")).thenReturn(new MovieSubtitle());
		assertThrows(AlreadyUploaded.class, () -> handler.uploadFile(new FileUploadCommand().filename("alreadyUploaded.srt").owner("me")));
	}
}
