package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@EqualsAndHashCode(of = "id")
@Data
public class User {

    private Long id;

    @NotEmpty(message = "Email cannot be empty or null")
    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Login must match {regexp}")
    @NotNull(message = "Login cannot be null")
    private String login;

    private String name;

    @Past(message = "Your birthday should be in past")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthday;

}