package xyz.zlatanov.subsbuddy.command.delete;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

@ExtendWith(MockitoExtension.class)
class DeleteFileCommandHandlerImplTest {

	@Mock
	MovieSubtitleRepository			movieSubtitleRepository;
	@InjectMocks
	DeleteFileCommandHandlerImpl	handler;
	UUID							subtitleId	= UUID.randomUUID();

	@Test
	void execute_deleteFile_returns() {
		doNothing().when(movieSubtitleRepository).deleteAllByTranslatedFromSubtitleId(subtitleId);
		doNothing().when(movieSubtitleRepository).deleteById(subtitleId);
		assertDoesNotThrow(() -> handler.execute(new DeleteFileCommand().id(subtitleId)));
	}

}