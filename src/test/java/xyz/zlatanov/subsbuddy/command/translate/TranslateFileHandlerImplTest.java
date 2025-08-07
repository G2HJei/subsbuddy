package xyz.zlatanov.subsbuddy.command.translate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static xyz.zlatanov.subsbuddy.domain.Language.BG;
import static xyz.zlatanov.subsbuddy.domain.Language.EN;
import static xyz.zlatanov.subsbuddy.domain.Translation.Status.FAILED;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.domain.Translation;
import xyz.zlatanov.subsbuddy.exception.TranslationException;
import xyz.zlatanov.subsbuddy.query.assemblesubs.AssembleSubsQueryHandler;
import xyz.zlatanov.subsbuddy.query.assemblesubs.AssembleSubsQueryProjection;
import xyz.zlatanov.subsbuddy.query.parselines.ParseLinesProjection;
import xyz.zlatanov.subsbuddy.query.parselines.ParseLinesQueryHandler;
import xyz.zlatanov.subsbuddy.query.translatetext.TranslateTextProjection;
import xyz.zlatanov.subsbuddy.query.translatetext.TranslateTextQueryHandler;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;
import xyz.zlatanov.subsbuddy.repository.TranslationRepository;

class TranslateFileHandlerImplTest {

	MovieSubtitleRepository		movieSubtitleRepository		= mock(MovieSubtitleRepository.class);
	TranslationRepository		translationRepository		= mock(TranslationRepository.class);
	ParseLinesQueryHandler		parseLinesQueryHandler		= mock(ParseLinesQueryHandler.class);
	TranslateTextQueryHandler	translateTextQueryHandler	= mock(TranslateTextQueryHandler.class);
	AssembleSubsQueryHandler	assembleSubsQueryHandler	= mock(AssembleSubsQueryHandler.class);

	TranslateOrchestratorAsync	translateOrchestratorAsync	= new TranslateOrchestratorAsync(
			movieSubtitleRepository, translationRepository, parseLinesQueryHandler, translateTextQueryHandler, assembleSubsQueryHandler);
	TranslateFileCommandHandler	handler						= new TranslateFileCommandHandlerImpl(
			movieSubtitleRepository, translationRepository, translateOrchestratorAsync);

	String						fileId						= "test";
	TranslateFileCommand		command						= new TranslateFileCommand().id(fileId);
	String						subtitleData				= """
			1
			00:01:07,818 --> 00:01:11,572
			It all began with the forging\s
			of the Great Rings...
			""";
	MovieSubtitle				lotrEn						= new MovieSubtitle()
			.id(fileId)
			.filename("test.srt")
			.language(EN)
			.subtitleData(subtitleData);

	@Test
	void execute_existingFile_translates() {
		when(movieSubtitleRepository.findById(fileId)).thenReturn(Optional.of(lotrEn));
		when(translationRepository.findBySourceId(fileId)).thenReturn(List.of());
		when(translationRepository.findOneBySourceHashCodeAndStatusNot(subtitleData.hashCode(), FAILED)).thenReturn(null);
		when(parseLinesQueryHandler.execute(any())).thenReturn(new ParseLinesProjection());
		when(translateTextQueryHandler.execute(any())).thenReturn(new TranslateTextProjection());
		when(assembleSubsQueryHandler.execute(any())).thenReturn(new AssembleSubsQueryProjection());
		when(movieSubtitleRepository.save(any())).thenReturn(new MovieSubtitle().id("saved"));
		when(translationRepository.save(any())).thenReturn(new Translation());
		assertDoesNotThrow(() -> handler.execute(command));
	}

	@Test
	void execute_alreadyTranslated_returns() {
		when(movieSubtitleRepository.findById(fileId)).thenReturn(Optional.of(lotrEn));
		when(translationRepository.findBySourceId(fileId)).thenReturn(List.of());
		when(translationRepository.findOneBySourceHashCodeAndStatusNot(subtitleData.hashCode(), FAILED))
				.thenReturn(new Translation().id("translated"));
		when(translationRepository.findOneBySourceIdAndTranslationId("test", "translated")).thenReturn(null);
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
		when(translationRepository.findBySourceId(fileId)).thenReturn(List.of(new Translation()));
		assertThrows(TranslationException.class, () -> handler.execute(command));
	}

	@Test
	void execute_nonEnglish_throws() {
		when(movieSubtitleRepository.findById(fileId)).thenReturn(Optional.of(new MovieSubtitle().id(fileId).language(BG)));
		assertThrows(TranslationException.class, () -> handler.execute(command));
	}
}