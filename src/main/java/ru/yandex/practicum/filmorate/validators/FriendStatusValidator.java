package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.annotations.CorrectFriendStatus;
import ru.yandex.practicum.filmorate.model.utils.FriendStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;

public class FriendStatusValidator implements ConstraintValidator<CorrectFriendStatus, String> {
    @Override
    public boolean isValid(String status,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (status == null || status.equals(""))
            return true;
        return FriendStatus.isFriendStatus(status);
    }
}

