package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Motion Picture Association - МРА:</p>
 * <p>G — у фильма нет возрастных ограничений,</p>
 * <p>PG — детям рекомендуется смотреть фильм с родителями,</p>
 * <p>PG-13 — детям до 13 лет просмотр не желателен,</p>
 * <p>R — лицам до 17 лет просматривать фильм можно только в присутствии взрослого,</p>
 * <p>NC-17 — лицам до 18 лет просмотр запрещён</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    private Long id;

    @NotNull
    private String name;
}
