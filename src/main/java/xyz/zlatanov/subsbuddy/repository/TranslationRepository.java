package xyz.zlatanov.subsbuddy.repository;

import static xyz.zlatanov.subsbuddy.domain.Translation.Status;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.zlatanov.subsbuddy.domain.Translation;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, UUID> {

	List<Translation> findBySourceSubtitleId(UUID sourceId);

	Translation findOneBySourceSubtitleIdAndTranslatedSubtitleId(UUID sourceId, UUID translationId);

	void deleteAllBySourceSubtitleId(UUID sourceId);

	Translation findOneBySourceHashCodeAndStatusNot(int hashCode, Status status);
}
