package xyz.zlatanov.subsbuddy.query.availablesubs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static xyz.zlatanov.subsbuddy.domain.Language.ENGLISH;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

class AvailableSubsQueryHandlerImplTest {

	MovieSubtitleRepository		repository	= mock(MovieSubtitleRepository.class);
	AvailableSubsQueryHandler	handler		= new AvailableSubsQueryHandlerImpl(repository);

	@BeforeEach
	void setup(){
		when(repository.findByOwner("owner1")).thenReturn(List.of(new MovieSubtitle().id("id").language(ENGLISH).filename("name")));
		when(repository.findByOwner("owner2")).thenReturn(List.of());
		when(repository.findByOwner(null)).thenReturn(List.of());
	}

	@Test
	void list_variableOwners_returnsSelectedOwner() {
		assertEquals(1, handler.list(new AvailableSubsQuery().owner("owner1")).result().size());
	}

	@Test
	void list_noSelectedOwner_returnsEmpty() {
		assertTrue(handler.list(new AvailableSubsQuery().owner(null)).result().isEmpty());

	}

	@Test
	void list_otherOwners_returnsEmpty() {
		assertTrue(handler.list(new AvailableSubsQuery().owner("owner2")).result().isEmpty());
	}
}