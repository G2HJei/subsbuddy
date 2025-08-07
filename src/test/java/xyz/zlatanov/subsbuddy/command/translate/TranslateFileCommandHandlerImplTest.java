package xyz.zlatanov.subsbuddy.command.translate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static xyz.zlatanov.subsbuddy.domain.Language.BG;
import static xyz.zlatanov.subsbuddy.domain.Language.EN;
import static xyz.zlatanov.subsbuddy.domain.Translation.Status.FAILED;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.val;
import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.domain.Translation;
import xyz.zlatanov.subsbuddy.exception.TranslationException;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;
import xyz.zlatanov.subsbuddy.repository.TranslationRepository;

@ExtendWith(MockitoExtension.class)
class TranslateFileCommandHandlerImplTest {

	@Mock
	MovieSubtitleRepository			movieSubtitleRepository;
	@Mock
	TranslationRepository			translationRepository;
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
		when(translationRepository.findBySourceSubtitleId(fileId)).thenReturn(List.of());
		when(translationRepository.findOneBySourceHashCodeAndStatusNot(subtitleData.hashCode(), FAILED)).thenReturn(null);
		val translation = new Translation();
		when(translationRepository.save(any())).thenReturn(translation);

		handler.execute(command);

		verify(translateOrchestratorAsync).orchestrateTranslation(lotrEn, translation);
	}

	@Test
	void execute_alreadyTranslated_returns() {
		val translatedId = UUID.randomUUID();
		when(movieSubtitleRepository.findById(fileId)).thenReturn(Optional.of(lotrEn));
		when(translationRepository.findBySourceSubtitleId(fileId)).thenReturn(List.of());
		when(translationRepository.findOneBySourceHashCodeAndStatusNot(subtitleData.hashCode(), FAILED))
				.thenReturn(new Translation().id(translatedId));
		when(translationRepository.save(any())).thenReturn(new Translation());
		assertDoesNotThrow(() -> handler.execute(command));
	}

	@Test
	void execute_notExistingFile_throws() {
		when(movieSubtitleRepository.findById(fileId)).thenReturn(Optional.empty());
		assertThrows(TranslationException.class, () -> handler.execute(command));
	}

	@Test
	void execute_alreadyTranslatedFile_throws() {
		when(movieSubtitleRepository.findById(fileId)).thenReturn(Optional.of(lotrEn));
		when(translationRepository.findBySourceSubtitleId(fileId)).thenReturn(List.of(new Translation()));
		assertThrows(TranslationException.class, () -> handler.execute(command));
	}

	@Test
	void execute_nonEnglish_throws() {
		when(movieSubtitleRepository.findById(fileId)).thenReturn(Optional.of(new MovieSubtitle().id(fileId).language(BG)));
		assertThrows(TranslationException.class, () -> handler.execute(command));
	}
}