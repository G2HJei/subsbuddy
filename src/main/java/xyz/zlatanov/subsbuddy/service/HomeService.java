package xyz.zlatanov.subsbuddy.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.command.delete.DeleteFileCommand;
import xyz.zlatanov.subsbuddy.command.delete.DeleteFileCommandHandler;
import xyz.zlatanov.subsbuddy.command.upload.UploadFileCommand;
import xyz.zlatanov.subsbuddy.command.upload.UploadFileCommandHandler;
import xyz.zlatanov.subsbuddy.connector.TranslationConnector;
import xyz.zlatanov.subsbuddy.exception.NotSupportedFileTypeException;
import xyz.zlatanov.subsbuddy.model.DownloadModel;
import xyz.zlatanov.subsbuddy.model.HomeModel;
import xyz.zlatanov.subsbuddy.model.SubtitleModel;
import xyz.zlatanov.subsbuddy.model.TranslationModel;
import xyz.zlatanov.subsbuddy.query.availablesubs.AvailableSubsQuery;
import xyz.zlatanov.subsbuddy.query.availablesubs.AvailableSubsQueryHandler;
import xyz.zlatanov.subsbuddy.query.download.DownloadFileQueryHandler;
import xyz.zlatanov.subsbuddy.util.ReadUtils;

@Service
@AllArgsConstructor
public class HomeService {

	private AvailableSubsQueryHandler	availableSubsQueryHandler;
	private UploadFileCommandHandler	uploadFileCommandHandler;
	private DownloadFileQueryHandler	downloadFileQueryHandler;
	private DeleteFileCommandHandler	deleteFileCommandHandler;
	private TranslationConnector		translationConnector;

	public HomeModel getModel() {
		val quota = translationConnector.usagePercent();
		val availableSubs = availableSubsQueryHandler.execute(new AvailableSubsQuery());
		val subsList = availableSubs
				.result().stream()
				.map(d -> new SubtitleModel()
						.id(d.id().toString())
						.filename(d.filename())
						.language(d.language().name())
						.translations(d.translations().stream()
								.map(t -> new TranslationModel()
										.id(Optional.ofNullable(t.id()).map(UUID::toString).orElse(null))
										.language(t.language().name())
										.status(t.status().name()))
								.toList()))
				.toList();
		return new HomeModel()
				.quota(quota)
				.subtitleList(subsList);
	}

	public void upload(String fileName, byte[] bytes) {
		if (!fileName.endsWith(".srt")) {
			throw new NotSupportedFileTypeException();
		}
		uploadFileCommandHandler.execute(new UploadFileCommand()
				.filename(fileName)
				.content(ReadUtils.readUtf8Bytes(bytes)));
	}

	public DownloadModel download(String id) {
		val downloadProjection = downloadFileQueryHandler.execute(UUID.fromString(id));
		return new DownloadModel()
				.filename(downloadProjection.filename())
				.content(downloadProjection.content().getBytes(UTF_8));
	}

	public void delete(String id) {
		val idToDelete = UUID.fromString(id);
		deleteFileCommandHandler.execute(new DeleteFileCommand().id(idToDelete));
	}
}
