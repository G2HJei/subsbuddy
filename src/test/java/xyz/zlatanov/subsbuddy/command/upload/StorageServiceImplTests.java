package xyz.zlatanov.subsbuddy.command.upload;

import org.junit.jupiter.api.Test;

public class StorageServiceImplTests {

	UploadFileHandler handler = new UploadFileHandlerImpl();

	@Test
	void uploadService_validFile_returns() {
		// todo
		handler.uploadFile(new FileUploadCommand());
	}
}
