package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FilmLikes {

    private Long filmId;
    private Long userId;
}
