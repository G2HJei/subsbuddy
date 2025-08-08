package xyz.zlatanov.subsbuddy.command.translate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static xyz.zlatanov.subsbuddy.domain.Language.EN;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.val;
import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

@ExtendWith(MockitoExtension.class)
class TranslateFileCommandHandlerImplTest {

	@Mock
	MovieSubtitleRepository			movieSubtitleRepository;
	@Mock
	TranslateOrchestratorAsync		translateOrchestratorAsync;
	@InjectMocks
	TranslateFileCommandHandlerImpl	handler;

	UUID							fileId			= UUID.randomUUID();
	TranslateFileCommand			command			= new TranslateFileCommand().id(fileId);
	String							subtitleData	= """
			1
			00:01:07,818 --> 00:01:11,572
			It all began with the forging\s
			of the Great Rings...
			""";
	MovieSubtitle					lotrEn			= new MovieSubtitle()
			.id(fileId)
			.filename("test.srt")
			.language(EN)
			.subtitleData(subtitleData);

	@Test
	void execute_existingFile_translates() {
		when(movieSubtitleRepository.findById(fileId)).thenReturn(Optional.of(lotrEn));
		val translation = new MovieSubtitle();
		when(movieSubtitleRepository.save(any())).thenReturn(translation);

		handler.execute(command);

		verify(translateOrchestratorAsync).orchestrateTranslation(lotrEn, translation);
	}
}