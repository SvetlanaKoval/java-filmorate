package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    protected final Map<Long, Film> storage = new HashMap<>();

    @Override
    public Collection<Film> getAll() {
        return storage.values();
    }

    @Override
    public Film create(Film film) {
        if (storage.containsKey(film.getId())) {
            log.error("id {} already in use", (film.getId()));
            throw new DuplicatedDataException("Film with id " + film.getId() + " already exists");
        }

        film.setId(generateMaxId() + 1);
        storage.put(film.getId(), film);

        log.info("The new film {} has been added", film.getName());
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        Long newFilmId = newFilm.getId();
        if (newFilmId == null) {
            log.error("Did`t find film for updating because you send film with id = null");
            throw new ValidateException("Film should have an id");
        }
        Film oldFilm = storage.get(newFilmId);
        if (oldFilm == null) {
            log.error("Did`n find film with id {}", newFilmId);
            throw new NotFoundException("Can`t find film with id " + newFilmId);
        }

        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setDuration(newFilm.getDuration());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setLikes(newFilm.getLikes());
        oldFilm.setGenres(newFilm.getGenres());
        oldFilm.setRating(newFilm.getRating());

        log.info("The film {} has been updated", oldFilm.getName());
        return oldFilm;
    }

    @Override
    public Film delete(Film film) {
        Long deletedFilmId = film.getId();
        Film deletedFilm = storage.get(deletedFilmId);
        if (deletedFilm == null) {
            log.error("Did`n find film with id {}", deletedFilmId);
            throw new NotFoundException("Can`t find film with id " + deletedFilmId);
        }

        storage.remove(deletedFilmId);

        log.info("The film {} has been deleted", deletedFilm.getName());
        return deletedFilm;
    }

    @Override
    public Film getById(Long id) {
        return storage.values().stream()
            .filter(film -> film.getId().equals(id))
            .findFirst().orElseThrow(() ->
                new NotFoundException(String.format("Film with id - %d not found", id))
            );
    }

    private Long generateMaxId() {
        return (storage.keySet().stream()
            .max(Long::compare)
            .orElse(0L));
    }

}