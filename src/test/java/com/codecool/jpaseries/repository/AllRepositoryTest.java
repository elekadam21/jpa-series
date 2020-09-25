package com.codecool.jpaseries.repository;

import com.codecool.jpaseries.entity.Episode;
import com.codecool.jpaseries.entity.Genre;
import com.codecool.jpaseries.entity.Season;
import com.codecool.jpaseries.entity.Series;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class AllRepositoryTest {

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void saveOneSimple() {
        Episode episode = Episode.builder()
                .title("Episode one")
                .releaseDate(LocalDate.of(2010, 1, 1))
                .build();

        episodeRepository.save(episode);

        List<Episode> episodeList = episodeRepository.findAll();
        assertThat(episodeList).hasSize(1);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void titleShouldNotBeNull() {
        Episode episode = Episode.builder()
                .releaseDate(LocalDate.of(2010, 1, 1))
                .build();

        episodeRepository.save(episode);
    }

    @Test
    public void transientIsNotSaved() {
        Episode episode1 = Episode.builder()
                .title("Episode 1")
                .releaseDate(LocalDate.of(2010, 1, 1))
                .build();


        Episode episode2 = Episode.builder()
                .title("Episode 2")
                .releaseDate(LocalDate.of(2011, 1, 1))
                .build();

        Season season = Season.builder()
                .number(1)
                .episode(episode1)
                .episode(episode2)
                .build();
        season.countEpisodes();

        assertThat(season.getEpisodeNumber()).isEqualTo(2);

        seasonRepository.save(season);
        entityManager.clear();

        List<Season> seasons = seasonRepository.findAll();
        assertThat(seasons).allMatch(season1 -> season1.getEpisodeNumber() == 0L);
    }


    @Test
    public void episodeIsPersistedWithSeason() {
        Episode episode1 = Episode.builder()
                .title("Episode 1")
                .build();

        Season season1 = Season.builder()
                .number(1)
                .episode(episode1)
                .build();

        seasonRepository.save(season1);

        List<Episode> episodes = episodeRepository.findAll();
        assertThat(episodes)
                .hasSize(1)
                .allMatch(episode -> episode.getId() > 0L);
    }

    @Test
    public void seasonsArePersistedAndDeletedWithNewSeries() {
        Set<Season> seasons = IntStream.range(1, 10)
                .boxed()
                .map(integer -> Season.builder().number(integer).build())
                .collect(Collectors.toSet());

        Series series1 = Series.builder()
                .name("Series 1")
                .genre(Genre.FANTASY)
                .seasons(seasons)
                .build();

        seriesRepository.save(series1);

        assertThat(seasonRepository.findAll())
                .hasSize(9)
                .anyMatch(season -> season.getNumber().equals(9));

        seriesRepository.deleteAll();

        assertThat(seasonRepository.findAll())
                .hasSize(0);
    }
}