package xyz.zlatanov.subsbuddy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import xyz.zlatanov.subsbuddy.command.delete.DeleteFileCommand;
import xyz.zlatanov.subsbuddy.command.delete.DeleteFileHandler;
import xyz.zlatanov.subsbuddy.command.upload.UploadFileCommand;
import xyz.zlatanov.subsbuddy.command.upload.UploadFileHandler;
import xyz.zlatanov.subsbuddy.exception.NotSupportedFileType;
import xyz.zlatanov.subsbuddy.model.SubtitleFileModel;
import xyz.zlatanov.subsbuddy.query.availablesubs.AvailableSubsQuery;
import xyz.zlatanov.subsbuddy.query.availablesubs.AvailableSubsQueryHandler;
import xyz.zlatanov.subsbuddy.util.ReadUtils;

@Service
@AllArgsConstructor
public class HomeService {

	private AvailableSubsQueryHandler	availableSubsQueryHandler;
	private UploadFileHandler			uploadFileHandler;
	private DeleteFileHandler			deleteFileHandler;

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
		uploadFileHandler.uploadFile(new UploadFileCommand()
				.filename(fileName)
				.content(ReadUtils.readUtf8Bytes(bytes))
				.owner(owner));
	}

	public void delete(String id) {
		deleteFileHandler.deleteFile(new DeleteFileCommand().id(id));
	}
}
