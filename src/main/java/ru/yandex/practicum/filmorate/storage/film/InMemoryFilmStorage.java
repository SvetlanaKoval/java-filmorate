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
    private long idGenerator = 0;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getFilms() {
        log.info("Getting all films");
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
        log.info("Adding new film");
        if (films.containsKey(film.getId())) {
            log.error("id {} already in use", (film.getId()));
            throw new DuplicatedDataException("Film with id " + film.getId() + " already exists");
        }

        film.setId(++idGenerator);
        films.put(film.getId(), film);

        log.info("The new film {} has been added", film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        log.info("Updating film");
        Long newFilmId = newFilm.getId();
        if (newFilmId == null) {
            log.error("Did`t find film for updating because you send film with id = null");
            throw new ValidateException("Film should have an id");
        }
        Film oldFilm = films.get(newFilmId);
        if (oldFilm == null) {
            log.error("Did`n find film with id {}", newFilmId);
            throw new NotFoundException("Can`t find film with id " + newFilmId);
        }

        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setDuration(newFilm.getDuration());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());

        log.info("The film {} has been updated", oldFilm.getName());
        return oldFilm;
    }

    @Override
    public Film deleteFilm(Film film) {
        log.info("Deleting film");
        long deletedFilmId = film.getId();
        Film deletedFilm = films.get(deletedFilmId);
        if (deletedFilm == null) {
            log.error("Did`n find film with id {}", deletedFilmId);
            throw new NotFoundException("Can`t find film with id " + deletedFilmId);
        }

        films.remove(deletedFilmId);

        log.info("The film {} has been deleted", deletedFilm.getName());
        return deletedFilm;
    }
}
