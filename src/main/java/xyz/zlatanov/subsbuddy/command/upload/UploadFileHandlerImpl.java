package xyz.zlatanov.subsbuddy.command.upload;

import static xyz.zlatanov.subsbuddy.domain.Language.ENGLISH;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.exception.AlreadyUploaded;
import xyz.zlatanov.subsbuddy.exception.NonEnglishSubtitleUploaded;
import xyz.zlatanov.subsbuddy.exception.NotSupportedFileType;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;
import xyz.zlatanov.subsbuddy.util.ReadUtils;

@Service
@AllArgsConstructor
public class UploadFileHandlerImpl implements UploadFileHandler {

	private MovieSubtitleRepository movieSubtitleRepository;

	@Override
	@Transactional
	public void uploadFile(FileUploadCommand file) {
		validateCommand(file);
		movieSubtitleRepository.insert(new MovieSubtitle()
				.filename(file.filename())
				.language(ENGLISH)
				.subtitleData(file.content())
				.owner(file.owner()));
	}

	private void validateCommand(FileUploadCommand file) {
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
