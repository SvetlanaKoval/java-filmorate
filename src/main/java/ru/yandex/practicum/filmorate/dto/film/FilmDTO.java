package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class FilmDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    private Integer duration;

    private MpaDTO mpa;

    private List<GenreDTO> genres = List.of();

}
