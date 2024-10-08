package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

/**
 * Film.
 */
@Data
public class Film {

    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, Month.DECEMBER, 28);

    private Long id;

    @NotBlank(message = "Film`s name cannot be null")
    private String name;

    @Size(min = 1, max = 200, message = "Description length should be less or equal then 200")
    private String description;

    @PastOrPresent(message = "Film release should be in past")
    private LocalDate releaseDate;

    @Positive(message = "Film`s duration should be positive")
    private Integer duration;

    @NotEmpty(message = "Film should have at last one genre")
    private List<Genre> genres = List.of();

    @NotNull(message = "Film should have Motion Picture Association rating")
    private Rating rating;

    @AssertTrue(message = "Release date should be after 28.12.1895")
    private boolean isRightReleaseDate() {
        return this.releaseDate.isAfter(MIN_DATE_RELEASE);
    }

}