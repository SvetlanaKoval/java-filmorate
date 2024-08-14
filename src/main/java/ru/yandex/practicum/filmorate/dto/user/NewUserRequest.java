package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDate;

@Data
public class NewUserRequest {

    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Login must match {regexp}")
    private String login;

    @PastOrPresent(message = "Your birthday should be in past")
    private LocalDate birthday;

}
