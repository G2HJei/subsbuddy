package xyz.zlatanov.subsbuddy.web.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.core.SubsBuddyClient;
import xyz.zlatanov.subsbuddy.core.util.ReadUtils;
import xyz.zlatanov.subsbuddy.web.command.delete.DeleteSubtitleCommandHandler;
import xyz.zlatanov.subsbuddy.web.command.upload.UploadFileCommand;
import xyz.zlatanov.subsbuddy.web.command.upload.UploadFileCommandHandler;
import xyz.zlatanov.subsbuddy.web.exception.NotSupportedFileTypeException;
import xyz.zlatanov.subsbuddy.web.model.DownloadModel;
import xyz.zlatanov.subsbuddy.web.model.HomeModel;
import xyz.zlatanov.subsbuddy.web.model.SubtitleModel;
import xyz.zlatanov.subsbuddy.web.model.TranslationModel;
import xyz.zlatanov.subsbuddy.web.query.availablesubs.AvailableSubsQuery;
import xyz.zlatanov.subsbuddy.web.query.availablesubs.AvailableSubsQueryHandler;
import xyz.zlatanov.subsbuddy.web.query.download.DownloadFileQueryHandler;

@Service
@RequiredArgsConstructor
public class HomeService {

	private final SubsBuddyClient				client;
	private final AvailableSubsQueryHandler		availableSubsQueryHandler;
	private final UploadFileCommandHandler		uploadFileCommandHandler;
	private final DownloadFileQueryHandler		downloadFileQueryHandler;
	private final DeleteSubtitleCommandHandler	deleteSubtitleCommandHandler;

	public HomeModel getModel() {
		val quota = 100 - client.usagePercent();
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
		deleteSubtitleCommandHandler.execute(idToDelete);
	}
}
