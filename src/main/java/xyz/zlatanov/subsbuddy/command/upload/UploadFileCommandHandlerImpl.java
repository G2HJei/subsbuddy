package xyz.zlatanov.subsbuddy.command.upload;

import static xyz.zlatanov.subsbuddy.domain.Language.EN;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.command.translate.TranslateFileCommand;
import xyz.zlatanov.subsbuddy.command.translate.TranslateFileCommandHandler;
import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.exception.AlreadyUploadedException;
import xyz.zlatanov.subsbuddy.exception.NonEnglishSubtitleUploadedException;
import xyz.zlatanov.subsbuddy.exception.NotSupportedFileTypeException;
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
		val sub = movieSubtitleRepository.save(new MovieSubtitle()
				.filename(file.filename())
				.language(EN)
				.subtitleData(file.content()));
		translateFileCommandHandler.execute(new TranslateFileCommand().id(sub.id()));
	}

	private void validateCommand(UploadFileCommand file) {
		if (!file.filename().endsWith(".srt")) {
			throw new NotSupportedFileTypeException();
		}
		if (movieSubtitleRepository.findOneByFilename(file.filename()) != null) {
			throw new AlreadyUploadedException();
		}
		if (!ReadUtils.hasEnglishCharacters(file.content(), 0.2)) {
			throw new NonEnglishSubtitleUploadedException();
		}
	}
}
