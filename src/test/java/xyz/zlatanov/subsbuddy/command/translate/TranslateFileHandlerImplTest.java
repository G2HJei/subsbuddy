package xyz.zlatanov.subsbuddy.command.translate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static xyz.zlatanov.subsbuddy.domain.Language.BG;
import static xyz.zlatanov.subsbuddy.domain.Language.EN;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.domain.Translation;
import xyz.zlatanov.subsbuddy.exception.TranslationException;
import xyz.zlatanov.subsbuddy.query.formatsubscontent.FormatSubsQueryHandler;
import xyz.zlatanov.subsbuddy.query.formatsubscontent.FormatSubsQueryProjection;
import xyz.zlatanov.subsbuddy.query.splitlines.SplitLinesProjection;
import xyz.zlatanov.subsbuddy.query.splitlines.SplitLinesQueryHandler;
import xyz.zlatanov.subsbuddy.query.translatetext.TranslateTextProjection;
import xyz.zlatanov.subsbuddy.query.translatetext.TranslateTextQueryHandler;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;
import xyz.zlatanov.subsbuddy.repository.TranslationRepository;

class TranslateFileHandlerImplTest {

	MovieSubtitleRepository		movieSubtitleRepository		= mock(MovieSubtitleRepository.class);
	TranslationRepository		translationRepository		= mock(TranslationRepository.class);
	SplitLinesQueryHandler		splitLinesQueryHandler		= mock(SplitLinesQueryHandler.class);
	TranslateTextQueryHandler	translateTextQueryHandler	= mock(TranslateTextQueryHandler.class);
	FormatSubsQueryHandler		formatSubsQueryHandler		= mock(FormatSubsQueryHandler.class);

	TranslateFileCommandHandler	handler						= new TranslateFileCommandHandlerImpl(movieSubtitleRepository,
			translationRepository, splitLinesQueryHandler, translateTextQueryHandler, formatSubsQueryHandler);

	String						fileId						= "test";
	TranslateFileCommand		command						= new TranslateFileCommand().id(fileId);
	MovieSubtitle				lotrEn						= new MovieSubtitle()
			.id(fileId)
			.owner(fileId)
			.filename("test.srt")
			.language(EN)
			.subtitleData("""
					1
					00:01:07,818 --> 00:01:11,572
					It all began with the forging\s
					of the Great Rings...
					""");

	@Test
	void translateFile_existingFile_translates() {
		when(movieSubtitleRepository.findById(fileId)).thenReturn(Optional.of(lotrEn));
		when(translationRepository.findBySourceId(fileId)).thenReturn(List.of());
		when(splitLinesQueryHandler.execute(any())).thenReturn(new SplitLinesProjection());
		when(translateTextQueryHandler.execute(any())).thenReturn(new TranslateTextProjection());
		when(formatSubsQueryHandler.execute(any())).thenReturn(new FormatSubsQueryProjection());
		when(movieSubtitleRepository.save(any())).thenReturn(new MovieSubtitle().id("saved"));
		when(translationRepository.save(any())).thenReturn(new Translation());
		assertDoesNotThrow(() -> handler.execute(command));
	}

	@Test
	void translateFile_notExistingFile_throws() {
		when(movieSubtitleRepository.findById(fileId)).thenReturn(Optional.empty());
		assertThrows(TranslationException.class, () -> handler.execute(command));
	}

	@Test
	void translateFile_alreadyTranslatedFile_throws() { 	
		when(movieSubtitleRepository.findById(fileId)).thenReturn(Optional.of(lotrEn));
		when(translationRepository.findBySourceId(fileId)).thenReturn(List.of(new Translation()));
		assertThrows(TranslationException.class, () -> handler.execute(command));
	}

	@Test
	void translateFile_nonEnglish_throws(){
		when(movieSubtitleRepository.findById(fileId)).thenReturn(Optional.of(new MovieSubtitle().id(fileId).language(BG)));
		assertThrows(TranslationException.class, () -> handler.execute(command));
	}
}