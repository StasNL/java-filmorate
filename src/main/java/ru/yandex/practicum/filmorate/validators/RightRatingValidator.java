package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.annotations.RightGenre;
import ru.yandex.practicum.filmorate.annotations.RightRating;
import ru.yandex.practicum.filmorate.model.utils.Rating;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RightRatingValidator implements ConstraintValidator<RightRating, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null)
            return true;
        return Rating.isRating(s);
    }
}
