package ru.job4j.dreamjob.store;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class PostStore {

    private static AtomicInteger id = new AtomicInteger(4);
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private PostStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Low level", LocalDate.now().plusDays(1)));
        posts.put(2, new Post(2, "Middle Java Job", "Average level", LocalDate.now().plusDays(2)));
        posts.put(3, new Post(3, "Senior Java Job", "Difficult level", LocalDate.now().plusDays(3)));
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public void add(Post post) {
        post.setId(id.getAndIncrement());
        post.setCreated(LocalDate.now());
        posts.put(post.getId(), post);
    }

    public Object findById(int id) {
        return posts.get(id);
    }

    public void update(Post post) {
        posts.put(post.getId(), post);
    }
}
