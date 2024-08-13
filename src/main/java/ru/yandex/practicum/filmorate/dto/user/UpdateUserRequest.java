package ru.yandex.practicum.filmorate.dto.user;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import java.time.LocalDate;
import java.util.Set;

@Data
public class UpdateUserRequest {

    private Long id;

    private String name;

    private String email;

    private String login;

    private LocalDate birthday;

    private Set<Long> favouriteFilmsId;

    public boolean hasName() {
        return StringUtils.isNotBlank(name);
    }

    public boolean hasEmail() {
        return StringUtils.isNotBlank(email);
    }

    public boolean hasLogin() {
        return StringUtils.isNotBlank(login);
    }

    public boolean hasBirthday() {
        return birthday != null;
    }

}
