package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.film.FilmDTO;
import ru.yandex.practicum.filmorate.dto.film.GenreDTO;
import ru.yandex.practicum.filmorate.dto.film.MpaDTO;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import java.util.List;

public class FilmMapper {

    public static FilmDTO toDTO(Film film) {
        FilmDTO dto = new FilmDTO();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        dto.setLikes(film.getLikes());

        MpaDTO mpa = new MpaDTO(film.getRating().getId(), film.getRating().getName());
        dto.setMpa(mpa);

        List<GenreDTO> genres = film.getGenres().stream()
            .map(genre -> new GenreDTO(genre.getId(), genre.getName()))
            .toList();
        dto.setGenres(genres);

        return dto;
    }

    public static Film toFilm(NewFilmRequest request, Rating rating, List<Genre> genres) {
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setReleaseDate(request.getReleaseDate());
        film.setDuration(request.getDuration());
        film.setRating(rating);
        film.setGenres(genres);

        return film;
    }

    public static Film updateFilmFields(Film film, UpdateFilmRequest request, Rating rating, List<Genre> genres) {
        if (request.hasName()) {
            film.setName(request.getName());
        }

        if (request.hasDescription()) {
            film.setDescription(request.getDescription());
        }

        if (request.hasReleaseDate()) {
            film.setReleaseDate(request.getReleaseDate());
        }

        if (request.hasDuration()) {
            film.setDuration(request.getDuration());
        }

        if (request.hasRating()) {
            film.setRating(rating);
        }

        if (request.hasGenres()) {
            film.setGenres(genres);
        }

        return film;
    }

}