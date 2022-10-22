package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.annotations.NotWhiteSpace;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotBlankValidator implements ConstraintValidator<NotWhiteSpace, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext cvc) {
        return !s.contains(" ");
    }
}
