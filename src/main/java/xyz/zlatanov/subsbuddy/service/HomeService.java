package xyz.zlatanov.subsbuddy.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.val;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import xyz.zlatanov.subsbuddy.command.delete.DeleteFileCommand;
import xyz.zlatanov.subsbuddy.command.delete.DeleteFileCommandHandler;
import xyz.zlatanov.subsbuddy.command.upload.UploadFileCommand;
import xyz.zlatanov.subsbuddy.command.upload.UploadFileCommandHandler;
import xyz.zlatanov.subsbuddy.exception.NotSupportedFileTypeException;
import xyz.zlatanov.subsbuddy.model.DownloadModel;
import xyz.zlatanov.subsbuddy.model.SubtitleModel;
import xyz.zlatanov.subsbuddy.query.availablesubs.AvailableSubsQuery;
import xyz.zlatanov.subsbuddy.query.availablesubs.AvailableSubsQueryHandler;
import xyz.zlatanov.subsbuddy.query.download.DownloadFileQueryHandler;
import xyz.zlatanov.subsbuddy.util.ReadUtils;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@AllArgsConstructor
public class HomeService {

	private AvailableSubsQueryHandler	availableSubsQueryHandler;
	private UploadFileCommandHandler	uploadFileCommandHandler;
	private DownloadFileQueryHandler	downloadFileQueryHandler;
	private DeleteFileCommandHandler	deleteFileCommandHandler;

	public List<SubtitleModel> getModel(String owner) {
		return availableSubsQueryHandler.execute(new AvailableSubsQuery().owner(owner))
				.result().stream()
				.map(d -> new SubtitleModel()
						.id(d.id())
						.filename(d.filename())
						.language(d.language())
						.translations(d.translations().entrySet().stream()
								.collect(Collectors.toMap(k -> k.getKey().name(), Map.Entry::getValue))))
				.toList();
	}

	public void upload(String fileName, byte[] bytes, String owner) {
		if (!fileName.endsWith(".srt")) {
			throw new NotSupportedFileTypeException();
		}
		uploadFileCommandHandler.execute(new UploadFileCommand()
				.filename(fileName)
				.content(ReadUtils.readUtf8Bytes(bytes))
				.owner(owner));
	}

	public DownloadModel download(String id) {
		val downloadProjection = downloadFileQueryHandler.execute(id);
		return new DownloadModel()
				.filename(downloadProjection.filename())
				.content(downloadProjection.content().getBytes(UTF_8));
	}

	public void delete(String id) {
		deleteFileCommandHandler.execute(new DeleteFileCommand().id(id));
	}
}
