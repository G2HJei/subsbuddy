package xyz.zlatanov.subsbuddy.command.upload;

import org.junit.jupiter.api.Test;

public class StorageServiceImplTests {

	StorageService service = new StorageServiceImpl();

	@Test
	void uploadService_validFile_returns() {
		// todo
		service.uploadFile(new FileUploadCommand());
	}
}
