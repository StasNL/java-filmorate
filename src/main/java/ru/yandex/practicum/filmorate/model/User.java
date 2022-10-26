package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.annotations.NotWhiteSpace;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @NonNull
    @PositiveOrZero
    private long id;

    @NonNull
    @NotEmpty(message = "email shouldn't be empty")
    @Email(message = "email should contain @")
    private String email;

    @NonNull
    @NotEmpty(message = "Login shouldn't be empty")
    @NotWhiteSpace(message = "Login shouldn't contain spaces")
    private String login;

    @NonNull
    @PastOrPresent(message = "Birthday shouldn't be in the future")
    private LocalDate birthday;

    private final Set<Long> friends = new HashSet<>();

    private String name;

    public String checkName() {
        if(name == null || name.isEmpty()) {
            name = login;
        }
        return name;
    }
}
