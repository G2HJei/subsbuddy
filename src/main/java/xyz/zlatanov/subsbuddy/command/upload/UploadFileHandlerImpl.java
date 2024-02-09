package xyz.zlatanov.subsbuddy.command.upload;

import static xyz.zlatanov.subsbuddy.domain.FileType.SRT;

import org.springframework.stereotype.Service;

import xyz.zlatanov.subsbuddy.exception.NotSupportedFileType;

@Service
public class UploadFileHandlerImpl implements UploadFileHandler {

	@Override
	public void uploadFile(FileUploadCommand file) {
		if (!file.filename().endsWith(".srt") || file.type() != SRT) {
			throw new NotSupportedFileType();
		}
	}
}
