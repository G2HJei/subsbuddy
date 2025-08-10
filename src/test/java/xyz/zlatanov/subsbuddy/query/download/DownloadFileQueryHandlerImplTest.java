package xyz.zlatanov.subsbuddy.query.download;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import lombok.val;
import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.exception.SubtitleNotFoundException;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

class DownloadFileQueryHandlerImplTest {

	MovieSubtitleRepository		repository	= mock(MovieSubtitleRepository.class);
	DownloadFileQueryHandler	handler		= new DownloadFileQueryHandlerImpl(repository);

	UUID						subtitleId	= UUID.randomUUID();

	@Test
	void execute_validSub_returns() {
		val content = """
				1
				00:04:45,000 --> 00:04:48,000
				Last survivor of the Nostromo, signing off.
				""";
		when(repository.findById(subtitleId)).thenReturn(Optional.of(
				new MovieSubtitle()
						.name("Aliens (1986) HDRip XviD.PSF-17.srt")
						.subtitleData(content)));
		val projection = handler.execute(subtitleId);
		assertEquals("Aliens (1986) HDRip XviD.PSF-17.srt", projection.filename());
		assertEquals(content, projection.content());
	}

	@Test
	void execute_missingSub_throws() {
		when(repository.findById(subtitleId)).thenReturn(Optional.empty());
		assertThrows(SubtitleNotFoundException.class, () -> handler.execute(subtitleId));
	}

}