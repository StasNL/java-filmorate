package ru.yandex.practicum.filmorate.exceptions.NotFoundException;

public enum ErrorType {
    USER, FILM, LIKES, MPA, GENRE, FRIENDS;

    public static String useType(ErrorType type) {
        if(type == USER)
            return NotFoundException.errorUserMessage;
        else if(type == FILM)
            return NotFoundException.errorFilmMessage;
        else if (type == LIKES)
            return NotFoundException.errorLikesMessage;
        else if (type == MPA)
            return NotFoundException.errorMpaMessage;
        else if (type == GENRE)
            return NotFoundException.errorGenreMessage;
        else
            return NotFoundException.errorFriendsMessage;
    }
}
