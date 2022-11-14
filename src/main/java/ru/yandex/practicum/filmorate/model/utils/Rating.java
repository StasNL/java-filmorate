package ru.yandex.practicum.filmorate.model.utils;

import java.util.HashMap;

public enum Rating {
    G("G"),
    PG("PG"),
    PG13("PG-13"),
    R("R"),
    NC17("NC-17");

    private String rating;
    private static final HashMap<String, Rating> RATINGS = new HashMap<>();

    Rating (String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    static {
        for (Rating r : values()){
            RATINGS.put(r.getRating(), r);
        }
    }

    public static boolean isRating(String rating) {
        return RATINGS.containsKey(rating);
    }
}
