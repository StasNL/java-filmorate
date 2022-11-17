package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.annotations.RightGenre;
import ru.yandex.practicum.filmorate.model.utils.Genre;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RightGenreValidator implements ConstraintValidator<RightGenre, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null || s.equals(""))
            return true;
        return Genre.isGenre(s);
    }
}
