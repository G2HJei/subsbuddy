package xyz.zlatanov.subsbuddy.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;

@Repository
public interface MovieSubtitleRepository extends MongoRepository<MovieSubtitle, String> {
}
