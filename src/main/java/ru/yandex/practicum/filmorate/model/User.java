package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.annotations.NotWhiteSpace;

import javax.validation.constraints.*;

@Data
@Builder
public class User {
    public final static String PATTERN_FOR_TIME = "dd.MM.yyyy";
    @NonNull
    private int id;

    @NonNull
    @NotEmpty(message = "email shouldn't be empty")
    @Email(message = "email should contain @")
    private String email;

    @NonNull
    @NotEmpty(message = "Login shouldn't be empty")
    @NotWhiteSpace(message = "Login shouldn't contain spaces")
    private String login;

    @NonNull
    @DateTimeFormat(pattern = PATTERN_FOR_TIME)
    @PastOrPresent(message = "Birthday shouldn't be in the future")
    private LocalDate birthday;

    private String name;

    public User update(User user) {
        this.email = user.email;
        this.login = user.login;
        if(name == null || name.isEmpty())
            this.name = user.login;
        else
            this.name = user.name;
        this.birthday = user.birthday;
        return this;
    }

    public User create(int userId) {
        this.id = userId;
        if(name == null || name.isEmpty())
            this.name = this.login;
        return this;
    }


}
