package xyz.zlatanov.subsbuddy.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.domain.TranslationStatus;

@Repository
public interface MovieSubtitleRepository extends JpaRepository<MovieSubtitle, UUID> {

	MovieSubtitle findOneByHashCode(int hashCode);

	void deleteAllByTranslatedFromSubtitleId(UUID id);

	List<MovieSubtitle> findByTranslatedFromSubtitleId(UUID translatedFromSubtitleId);

	List<MovieSubtitle> findByTranslatedFromSubtitleIdAndStatusNot(UUID translatedFromSubtitleId, TranslationStatus translationStatus);

}
