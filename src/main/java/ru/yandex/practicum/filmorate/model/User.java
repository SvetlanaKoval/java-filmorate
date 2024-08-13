package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDate;

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

    @PastOrPresent(message = "Your birthday should be in past")
    private LocalDate birthday;

}