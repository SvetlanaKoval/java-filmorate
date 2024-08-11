package ru.yandex.practicum.filmorate.dto.film;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateFilmRequest {

    private Long id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    private Integer duration;

    private MpaDTO mpa;

    private List<Long> likes = List.of();

    private List<GenreDTO> genres = List.of();

    public boolean hasName() {
        return StringUtils.isNotBlank(name);
    }

    public boolean hasDescription() {
        return StringUtils.isNotBlank(description);
    }

    public boolean hasReleaseDate() {
        return releaseDate != null;
    }

    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasRating() {
        return mpa != null;
    }

    public boolean hasLikes() {
        return !CollectionUtils.isEmpty(likes);
    }

    public boolean hasGenres() {
        return !CollectionUtils.isEmpty(genres);
    }
}
