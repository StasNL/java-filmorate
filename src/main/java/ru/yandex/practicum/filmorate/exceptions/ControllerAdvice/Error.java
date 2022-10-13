package ru.yandex.practicum.filmorate.exceptions.ControllerAdvice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Error {
    private final String fieldName;
    private final String message;
}

