package xyz.zlatanov.subsbuddy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import xyz.zlatanov.subsbuddy.command.delete.DeleteFileCommand;
import xyz.zlatanov.subsbuddy.command.delete.DeleteFileCommandHandler;
import xyz.zlatanov.subsbuddy.command.upload.UploadFileCommand;
import xyz.zlatanov.subsbuddy.command.upload.UploadFileCommandHandler;
import xyz.zlatanov.subsbuddy.exception.NotSupportedFileType;
import xyz.zlatanov.subsbuddy.model.SubtitleFileModel;
import xyz.zlatanov.subsbuddy.query.availablesubs.AvailableSubsQuery;
import xyz.zlatanov.subsbuddy.query.availablesubs.AvailableSubsQueryHandler;
import xyz.zlatanov.subsbuddy.util.ReadUtils;

@Service
@AllArgsConstructor
public class HomeService {

	private AvailableSubsQueryHandler	availableSubsQueryHandler;
	private UploadFileCommandHandler	uploadFileCommandHandler;
	private DeleteFileCommandHandler	deleteFileCommandHandler;

	public List<SubtitleFileModel> getModel(String owner) {
		return availableSubsQueryHandler.execute(new AvailableSubsQuery().owner(owner))
				.result().stream()
				.map(d -> new SubtitleFileModel(d.id(), d.filename(), d.language()))
				.toList();
	}

	public void upload(String fileName, byte[] bytes, String owner) {
		if (!fileName.endsWith(".srt")) {
			throw new NotSupportedFileType();
		}
		uploadFileCommandHandler.execute(new UploadFileCommand()
				.filename(fileName)
				.content(ReadUtils.readUtf8Bytes(bytes))
				.owner(owner));
	}

	public void delete(String id) {
		deleteFileCommandHandler.execute(new DeleteFileCommand().id(id));
	}
}
