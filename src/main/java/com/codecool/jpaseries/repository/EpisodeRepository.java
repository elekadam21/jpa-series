package com.codecool.jpaseries.repository;

import com.codecool.jpaseries.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {
}
