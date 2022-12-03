package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Post;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PostStore {

    private static AtomicInteger id = new AtomicInteger(4);

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

    public void add(Post post) {
        post.setId(id.getAndIncrement());
        post.setCreated(LocalDate.now());
        posts.put(post.getId(), post);
    }

    public Object findById(int id) {
        Object object = new Post();
        for (Map.Entry<Integer, Post> entry : posts.entrySet()) {
            Integer key = entry.getKey();
            if (key == id) {
                object = entry.getValue();
            }
        }
        return object;
    }

    public void update(Post post) {
        post.setCreated(LocalDate.now());
        posts.put(post.getId(), post);
    }
}
