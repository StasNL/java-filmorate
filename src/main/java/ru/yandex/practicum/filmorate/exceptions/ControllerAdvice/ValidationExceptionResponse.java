package ru.yandex.practicum.filmorate.exceptions.ControllerAdvice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ValidationExceptionResponse {

    private final List<Error> errors;
}

