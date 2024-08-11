package ru.yandex.practicum.filmorate.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.enums.FriendsStatus;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class UserDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String name;

    private String email;

    private String login;

    private LocalDate birthday;

    private Map<Long, FriendsStatus> friends = new HashMap<>();

    private Set<Long> favouriteFilmsId = new HashSet<>();
}
