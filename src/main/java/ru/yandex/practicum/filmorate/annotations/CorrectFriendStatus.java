package ru.yandex.practicum.filmorate.annotations;

import ru.yandex.practicum.filmorate.validators.FriendStatusValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE_USE, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = FriendStatusValidator.class)
@Documented
public @interface CorrectFriendStatus {
    String message() default "{Inappropriate friend status}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
