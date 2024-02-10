package xyz.zlatanov.subsbuddy.command.upload;

import static xyz.zlatanov.subsbuddy.domain.Language.ENGLISH;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.exception.NotSupportedFileType;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

@Service
@AllArgsConstructor
public class UploadFileHandlerImpl implements UploadFileHandler {

	private MovieSubtitleRepository movieSubtitleRepository;

	@Override
	@Transactional
	public void uploadFile(FileUploadCommand file) {
		if (!file.filename().endsWith(".srt")) {
			throw new NotSupportedFileType();
		}
		movieSubtitleRepository.insert(new MovieSubtitle(file.filename(), ENGLISH, file.content()));
	}
}
