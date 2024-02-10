package xyz.zlatanov.subsbuddy.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.command.upload.FileUploadCommand;
import xyz.zlatanov.subsbuddy.command.upload.UploadFileHandler;
import xyz.zlatanov.subsbuddy.util.ReadUtils;

@Service
@AllArgsConstructor
public class HomeService {

	private UploadFileHandler uploadFileHandler;

	public void upload(String fileName, byte[] bytes) {
		val content = ReadUtils.readUtf8Bytes(bytes);
		ReadUtils.hasEnglishCharacters(content, 0.2);
		uploadFileHandler.uploadFile(new FileUploadCommand()
				.filename(fileName)
				.content(content));
	}
}
