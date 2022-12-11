package ru.yandex.practicum.filmorate.exceptions.NotFoundException;

public class NotFoundException extends RuntimeException {
    protected static final String errorFilmMessage = "Фильм отсутствует в библиотеке.";
    protected static final String errorUserMessage = "Данный пользователь не зарегистрирован";

    protected static final String errorLikesMessage = "Лайка от данного пользователя не существует";

    protected static final String errorMpaMessage = "Рейтинг с таким id не существует.";
    protected static final String errorGenreMessage = "Жанр с таким id не существует.";
    protected static final String errorFriendsMessage = "У данного пользователя нет друзей.";

    public NotFoundException(String message) {
        super(message);
    }
}
