package xyz.zlatanov.subsbuddy.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import xyz.zlatanov.subsbuddy.domain.Translation;

@Repository
public interface TranslationRepository extends MongoRepository<Translation, String> {

	List<Translation> findBySourceId(String sourceId);
}
