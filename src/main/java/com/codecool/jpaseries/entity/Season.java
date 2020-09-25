package com.codecool.jpaseries.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Season {

    @Id
    @GeneratedValue
    private Long id;

    private Integer number;

    @ManyToOne
    private Series series;

    @Singular
    @OneToMany(mappedBy = "season", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @EqualsAndHashCode.Exclude
    private Set<Episode> episodes;

    @Transient
    private Integer episodeNumber;

    public void countEpisodes() {
        if (episodes != null)
            episodeNumber = episodes.size();
    }
}
