package ru.yandex.practicum.filmorate.annotations;

import ru.yandex.practicum.filmorate.validators.RightGenreValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = RightGenreValidator.class)
@Documented
public @interface RightGenre {

    String message() default "{Inappropriate genre}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

