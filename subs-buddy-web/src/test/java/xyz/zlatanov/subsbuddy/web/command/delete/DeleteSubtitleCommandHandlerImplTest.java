package xyz.zlatanov.subsbuddy.web.command.delete;

import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import xyz.zlatanov.subsbuddy.web.repository.MovieSubtitleRepository;

@ExtendWith(MockitoExtension.class)
class DeleteSubtitleCommandHandlerImplTest {

	@Mock
	MovieSubtitleRepository				movieSubtitleRepository;
	@InjectMocks
	DeleteSubtitleCommandHandlerImpl	handler;
	UUID								subId	= UUID.randomUUID();

	@Test
	void shouldDeleteSubtitle() {
		handler.execute(subId);
		verify(movieSubtitleRepository).deleteById(subId);
	}

	@Test
	void shouldDeleteSubtitleTranslations() {
		handler.execute(subId);
		verify(movieSubtitleRepository).deleteAllByTranslatedFromSubtitleId(subId);
	}

}