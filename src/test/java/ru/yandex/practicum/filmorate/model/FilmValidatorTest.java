package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmValidatorTest extends ValidatorTest {

    @Test
    public void nameNullValidation() {
        Film film = getCorrectFilledFilm();
        film.setName(null);

        Set<ConstraintViolation<Film>> constraintViolations = validator.validate(film);
        assertEquals(1, constraintViolations.size());

        String errorMessage = constraintViolations.iterator().next().getMessage();
        assertEquals("Film`s name cannot be null", errorMessage);
    }

    @Test
    public void nameEmptyValidation() {
        Film film = getCorrectFilledFilm();
        film.setName("");

        Set<ConstraintViolation<Film>> constraintViolations = validator.validate(film);
        assertEquals(1, constraintViolations.size());

        String errorMessage = constraintViolations.iterator().next().getMessage();
        assertEquals("Film`s name cannot be null", errorMessage);
    }

    @Test
    public void descriptionSizeValidation() {
        Film film = getCorrectFilledFilm();
        String desc = "wqryequywegqehf.lwqiehgaerhgpetjhpsehjotprojthero;yjhero;yjer;tjmnRLMNlrnmlmtyl'kjetYPjkety'pkj'yhmnghmt" +
            "lykjhgskwswgrkrgkerhgkerhgkegekjrgekjrgkejrgwqryequywegqehf.lwqiehgaerhgpetjhpsehjotprojthero;yjhero;yjer;tjmnRLMN" +
            "lrnmlmtyl'kjetYPjkety'pkj'yhmnghmtlykjhgskwswgrkrgkerhgkerhgkegekjrgekjrgkejrg";
        film.setDescription(desc);

        Set<ConstraintViolation<Film>> constraintViolations = validator.validate(film);
        assertEquals(1, constraintViolations.size());

        String errorMessage = constraintViolations.iterator().next().getMessage();
        assertEquals("Description length should be less or equal then 200", errorMessage);
    }

    @Test
    public void releaseDateAfterValidation() {
        Film film = getCorrectFilledFilm();
        film.setReleaseDate(LocalDate.of(1700, 1, 1));

        Set<ConstraintViolation<Film>> constraintViolations = validator.validate(film);
        assertEquals(1, constraintViolations.size());

        String errorMessage = constraintViolations.iterator().next().getMessage();
        assertEquals("Release date should be after 28.12.1895", errorMessage);
    }

    @Test
    public void releaseDatePastValidation() {
        Film film = getCorrectFilledFilm();
        film.setReleaseDate(LocalDate.of(2025, 1, 1));

        Set<ConstraintViolation<Film>> constraintViolations = validator.validate(film);
        assertEquals(1, constraintViolations.size());

        String errorMessage = constraintViolations.iterator().next().getMessage();
        assertEquals("Film release should be in past", errorMessage);
    }

    @Test
    public void positiveDurationValidation() {
        Film film = getCorrectFilledFilm();
        film.setDuration(0);

        Set<ConstraintViolation<Film>> constraintViolations = validator.validate(film);
        assertEquals(1, constraintViolations.size());

        String errorMessage = constraintViolations.iterator().next().getMessage();
        assertEquals("Film`s duration should be positive", errorMessage);
    }

    private Film getCorrectFilledFilm() {
        Film film = new Film();
        film.setName("Film");
        film.setDescription("qtwqyjeewlrujkgfhb.a");
        film.setReleaseDate(LocalDate.of(1990, 1, 1));
        film.setDuration(120);
        film.setRating(Rating.PG13);
        film.setGenres(Set.of(Genre.DRAMA, Genre.COMEDY));
        return film;
    }
}