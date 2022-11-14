package ru.yandex.practicum.filmorate.model.utils;

import lombok.Getter;

import java.util.HashMap;

@Getter
public enum FriendStatus {

    SUBSCRIBER("Subscriber"),
    SUBSCRIPTION("Subscription"),
    FRIEND("Friend");

    private String status;
    private static final HashMap<String, FriendStatus> STATUSES = new HashMap<>();

    FriendStatus(String friendStatus) {
        this.status = friendStatus;
    }

    static {
        for (FriendStatus status : values()) {
            STATUSES.put(status.getStatus(), status);
        }
    }

    public static boolean isFriendStatus(String s) {
        if(s == null)
            return false;
        return STATUSES.containsKey(s);
    }
}
