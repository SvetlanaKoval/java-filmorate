package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Жанры фильмов:</p>
 *
 * <p>Комедия - COMEDY</p>
 * <p>Драма - DRAMA</p>
 * <p>Мультфильм - CARTOON</p>
 * <p>Триллер - THRILLER</p>
 * <p>Документальный - DOCUMENTARY</p>
 * <p>Боевик - ACTION_MOVIE</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    private Long id;

    @NotNull
    private String name;
}
