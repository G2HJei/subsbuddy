package xyz.zlatanov.subsbuddy.repository;

import static xyz.zlatanov.subsbuddy.domain.Translation.Status;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.zlatanov.subsbuddy.domain.Translation;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, String> {

	List<Translation> findBySourceId(String sourceId);

	Translation findOneBySourceIdAndTranslatedId(String sourceId, String translationId);

	void deleteAllBySourceId(String sourceId);

	Translation findOneBySourceHashCodeAndStatusNot(int hashCode, Status status);
}
