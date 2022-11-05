package ru.yandex.practicum.filmorate.exceptions.NotFoundException;

public enum ErrorType {
    USER, FILM, LIKES;

    public static String useType(ErrorType type) {
        if(type == USER)
            return NotFoundException.errorUserMessage;
        else if(type == FILM)
            return NotFoundException.errorFilmMessage;
        else
            return NotFoundException.errorLikesMessage;
    }
}
