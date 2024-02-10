package xyz.zlatanov.subsbuddy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.command.upload.FileUploadCommand;
import xyz.zlatanov.subsbuddy.command.upload.UploadFileHandler;
import xyz.zlatanov.subsbuddy.exception.NonEnglishSubtitleUploaded;
import xyz.zlatanov.subsbuddy.exception.NotSupportedFileType;
import xyz.zlatanov.subsbuddy.model.SubtitleFileModel;
import xyz.zlatanov.subsbuddy.query.availablesubs.AvailableSubsQuery;
import xyz.zlatanov.subsbuddy.query.availablesubs.AvailableSubsQueryHandler;
import xyz.zlatanov.subsbuddy.util.ReadUtils;

@Service
@AllArgsConstructor
public class HomeService {

	private UploadFileHandler			uploadFileHandler;
	private AvailableSubsQueryHandler	availableSubsQueryHandler;

	public List<SubtitleFileModel> getModel(String owner) {
		return availableSubsQueryHandler.list(new AvailableSubsQuery().owner(owner))
				.result().stream()
				.map(d -> new SubtitleFileModel(d.id(), d.filename(), d.language()))
				.toList();
	}

	public void upload(String fileName, byte[] bytes, String owner) {
		if (!fileName.endsWith(".srt")) {
			throw new NotSupportedFileType();
		}
		val content = ReadUtils.readUtf8Bytes(bytes);
		if (!ReadUtils.hasEnglishCharacters(content, 0.2)) {
			throw new NonEnglishSubtitleUploaded();
		}
		uploadFileHandler.uploadFile(new FileUploadCommand()
				.filename(fileName)
				.content(content)
				.owner(owner));
	}
}
