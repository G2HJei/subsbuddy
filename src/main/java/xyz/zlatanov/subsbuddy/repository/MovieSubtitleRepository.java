package xyz.zlatanov.subsbuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;

@Repository
public interface MovieSubtitleRepository extends JpaRepository<MovieSubtitle, String> {

	MovieSubtitle findOneByFilename(String filename);
}
