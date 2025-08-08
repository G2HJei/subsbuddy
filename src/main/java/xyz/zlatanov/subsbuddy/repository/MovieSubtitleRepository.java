package xyz.zlatanov.subsbuddy.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.zlatanov.subsbuddy.domain.Language;
import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;

@Repository
public interface MovieSubtitleRepository extends JpaRepository<MovieSubtitle, UUID> {

	List<MovieSubtitle> findByLanguageOrderByCreatedAtDesc(Language language);

	void deleteAllByTranslatedFromSubtitleId(UUID id);

	List<MovieSubtitle> findByTranslatedFromSubtitleId(UUID translatedFromSubtitleId);

}
