package xyz.zlatanov.subsbuddy.command.upload;

import static xyz.zlatanov.subsbuddy.domain.Language.EN;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.command.translate.TranslateFileCommand;
import xyz.zlatanov.subsbuddy.command.translate.TranslateFileCommandHandler;
import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.exception.AlreadyUploaded;
import xyz.zlatanov.subsbuddy.exception.NonEnglishSubtitleUploaded;
import xyz.zlatanov.subsbuddy.exception.NotSupportedFileType;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;
import xyz.zlatanov.subsbuddy.util.ReadUtils;

@Service
@AllArgsConstructor
public class UploadFileCommandHandlerImpl implements UploadFileCommandHandler {

	private MovieSubtitleRepository		movieSubtitleRepository;
	private TranslateFileCommandHandler	translateFileCommandHandler;

	@Override
	@Transactional
	public void execute(UploadFileCommand file) {
		validateCommand(file);
		val sub = movieSubtitleRepository.insert(new MovieSubtitle()
				.filename(file.filename())
				.language(EN)
				.subtitleData(file.content())
				.owner(file.owner()));
		translateFileCommandHandler.execute(new TranslateFileCommand().id(sub.id()));
	}

	private void validateCommand(UploadFileCommand file) {
		if (!file.filename().endsWith(".srt")) {
			throw new NotSupportedFileType();
		}
		if (movieSubtitleRepository.findOneByOwnerAndFilename(file.owner(), file.filename()) != null) {
			throw new AlreadyUploaded();
		}
		if (!ReadUtils.hasEnglishCharacters(file.content(), 0.2)) {
			throw new NonEnglishSubtitleUploaded();
		}
	}
}
