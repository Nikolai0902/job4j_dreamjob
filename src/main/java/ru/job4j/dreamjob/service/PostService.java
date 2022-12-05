package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostStore;
import java.util.List;

public class PostService {
    private static final PostService INST = new PostService();

    private final PostStore store;

    private PostService() {
        store = PostStore.instOf();
    }

    public static PostService instOf() {
        return INST;
    }

    public List<Post> findAll() {
        return (List<Post>) store.findAll();
    }

    public void add(Post post) {
        store.add(post);
    }

    public Object findById(int id) {
        return store.findById(id);
    }

    public void update(Post post) {
        store.update(post);
    }
}
