package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Post;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PostStore {

    private static final PostStore INST = new PostStore();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private PostStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Low level", LocalDate.now().plusDays(1)));
        posts.put(2, new Post(2, "Middle Java Job", "Average level", LocalDate.now().plusDays(2)));
        posts.put(3, new Post(3, "Senior Java Job", "Difficult level", LocalDate.now().plusDays(3)));
    }

    public static PostStore instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }
}
