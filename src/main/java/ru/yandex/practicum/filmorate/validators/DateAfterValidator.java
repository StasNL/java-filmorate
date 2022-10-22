package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.annotations.DateAfter;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateAfterValidator implements ConstraintValidator<DateAfter, LocalDate> {

    private LocalDate dateToCompare;

    @Override
    public void initialize(DateAfter date) {
        String patternForTime = "dd.MM.yyyy";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(patternForTime);
        this.dateToCompare = LocalDate.parse(date.date(), dtf);

    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext cvc) {
        return date.isAfter(dateToCompare);
    }
}
