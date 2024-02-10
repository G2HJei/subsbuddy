package xyz.zlatanov.subsbuddy.command.upload;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import xyz.zlatanov.subsbuddy.exception.NotSupportedFileType;

public class FileUploadHandlerTest {

	UploadFileHandler handler = new UploadFileHandlerImpl();

	@Test
	void uploadFile_validFile_returns() {
		assertDoesNotThrow(() -> handler.uploadFile(new FileUploadCommand().filename("test.srt")));
	}

	@Test
	void uploadFile_nonSrtFile_throws() {
		assertThrows(NotSupportedFileType.class, () -> handler.uploadFile(new FileUploadCommand().filename("test.zip")));
	}
}
