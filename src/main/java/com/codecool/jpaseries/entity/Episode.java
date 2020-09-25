package com.codecool.jpaseries.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Episode {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    private LocalDate releaseDate;

    @ManyToOne
    private Season season;

    @ElementCollection
    @Singular
    private List<String> actors;

}
