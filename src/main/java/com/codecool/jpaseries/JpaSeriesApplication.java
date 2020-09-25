package com.codecool.jpaseries;

import com.codecool.jpaseries.entity.Episode;
import com.codecool.jpaseries.entity.Genre;
import com.codecool.jpaseries.entity.Season;
import com.codecool.jpaseries.entity.Series;
import com.codecool.jpaseries.repository.EpisodeRepository;
import com.codecool.jpaseries.repository.SeasonRepository;
import com.codecool.jpaseries.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

@SpringBootApplication
public class JpaSeriesApplication {

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    public static void main(String[] args) {
        SpringApplication.run(JpaSeriesApplication.class, args);
    }

    @Bean
    @Profile("production")
    public CommandLineRunner init() {
        return args -> {
            Episode episode1 = Episode.builder()
                    .title("Episode 1")
                    .actor("John")
                    .actor("Vilma")
                    .releaseDate(LocalDate.of(2010, 1, 1))
                    .build();

            Episode episode2 = Episode.builder()
                    .title("Episode 2")
                    .actor("James")
                    .actor("John")
                    .releaseDate(LocalDate.of(2011, 1, 1))
                    .build();

            Episode episode3 = Episode.builder()
                    .title("Episode 3")
                    .actor("Fred")
                    .releaseDate(LocalDate.of(2012, 1, 1))
                    .build();

            Episode episode4 = Episode.builder()
                    .title("Episode 4")
                    .releaseDate(LocalDate.of(2013, 1, 1))
                    .build();

            Season season1 = Season.builder()
                    .number(1)
                    .episode(episode1)
                    .episode(episode2)
                    .build();

            Season season2 = Season.builder()
                    .number(2)
                    .episode(episode3)
                    .episode(episode4)
                    .build();

            Series series1 = Series.builder()
                    .name("Series 1")
                    .genre(Genre.FANTASY)
                    .season(season1)
                    .season(season2)
                    .build();

            episode1.setSeason(season1);
            episode2.setSeason(season1);

            episode3.setSeason(season2);
            episode4.setSeason(season2);

            season1.setSeries(series1);
            season2.setSeries(series1);

            seriesRepository.save(series1);



        };
    }
}
