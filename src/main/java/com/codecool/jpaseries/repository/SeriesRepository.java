package com.codecool.jpaseries.repository;

import com.codecool.jpaseries.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<Series, Long> {
}
