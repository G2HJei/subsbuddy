package xyz.zlatanov.subsbuddy.web.command.upload;

import static xyz.zlatanov.subsbuddy.core.domain.Language.EN;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.core.util.ReadUtils;
import xyz.zlatanov.subsbuddy.web.command.translate.TranslateFileCommandHandler;
import xyz.zlatanov.subsbuddy.web.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.web.exception.NonEnglishSubtitleUploadedException;
import xyz.zlatanov.subsbuddy.web.exception.NotSupportedFileTypeException;
import xyz.zlatanov.subsbuddy.web.repository.MovieSubtitleRepository;

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
				.name(file.filename())
				.language(EN)
				.hashCode(file.content().hashCode())
				.subtitleData(file.content()));
		translateFileCommandHandler.execute(sub.id());
	}

	private void validateCommand(UploadFileCommand file) {
		if (!file.filename().endsWith(".srt")) {
			throw new NotSupportedFileTypeException();
		}
		if (!ReadUtils.hasEnglishCharacters(file.content(), 0.2)) {
			throw new NonEnglishSubtitleUploadedException();
		}
	}
}
