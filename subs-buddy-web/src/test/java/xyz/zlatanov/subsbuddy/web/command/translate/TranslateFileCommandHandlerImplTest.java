package xyz.zlatanov.subsbuddy.web.command.translate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.val;
import xyz.zlatanov.subsbuddy.web.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.web.repository.MovieSubtitleRepository;

@ExtendWith(MockitoExtension.class)
class TranslateFileCommandHandlerImplTest {

	@Mock
	MovieSubtitleRepository				movieSubtitleRepository;
	@Mock
	TranslateOrchestratorAsync			translateOrchestratorAsync;
	@InjectMocks
	TranslateSubtitleCommandHandlerImpl	handler;

	@Test
	void shouldTranslateExistingFile() {
		val subId = UUID.randomUUID();
		val sub = new MovieSubtitle().id(subId);
		when(movieSubtitleRepository.findById(subId)).thenReturn(Optional.of(sub));
		val translation = new MovieSubtitle();
		when(movieSubtitleRepository.save(any())).thenReturn(translation);

		handler.execute(subId);

		verify(translateOrchestratorAsync).orchestrateTranslation(sub, translation);
	}
}