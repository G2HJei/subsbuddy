package xyz.zlatanov.subsbuddy.command.upload;

import org.junit.jupiter.api.Test;

public class FileUploadHandlerTest {

	UploadFileHandler handler = new UploadFileHandlerImpl();

	@Test
	void fileUploadHandler_validFile_returns() {
		// todo
		handler.uploadFile(new FileUploadCommand());
	}
}
