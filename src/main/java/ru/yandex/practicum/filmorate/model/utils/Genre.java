package ru.yandex.practicum.filmorate.model.utils;

import lombok.Getter;

import java.util.HashMap;

@Getter
public enum Genre {
    COMEDY("Comedy"),
    DRAMA("Drama"),
    ANIMATION("Animation"),
    THRILLER("Thriller"),
    DOCUMENTARY("Documentary"),
    ACTION("Action");

    private String genre;
    private static final HashMap<String, Genre> GENRES = new HashMap<>();

    Genre (String genre) {
        this.genre = genre;
    }

    static {
        for (Genre g : values()) {
            GENRES.put(g.getGenre(), g);
        }
    }

    public static boolean isGenre(String genre) {
        return GENRES.containsKey(genre);
    }
}
