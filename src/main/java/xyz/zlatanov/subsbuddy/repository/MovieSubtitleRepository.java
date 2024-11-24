package xyz.zlatanov.subsbuddy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;

@Repository
public interface MovieSubtitleRepository extends JpaRepository<MovieSubtitle, String> {

	List<MovieSubtitle> findByOwner(String owner);

	MovieSubtitle findOneByOwnerAndFilename(String owner, String filename);
}
