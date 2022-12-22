package ru.job4j.dreamjob.config;

import ru.job4j.dreamjob.model.User;

import javax.servlet.http.HttpSession;

public final class Session {

    public Session() {
    }

    public static User getUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        return user;
    }
}
