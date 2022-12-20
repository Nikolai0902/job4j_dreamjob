package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.Test;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import ru.job4j.dreamjob.config.JdbcConfiguration;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class PostDBStoreTest {

    private final static BasicDataSource POOL = new JdbcConfiguration().loadPool();
    PostDBStore store = new PostDBStore(POOL);

    @Test
    public void whenCreatePost() {
        Post post = new Post(0, "Java Job", "6 years",
                LocalDate.of(2022, 12, 17), new City(1, "Moscow"));
        store.add(post);
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    public void whenUpdatePost() {
        Post post = new Post(0, "Java Job", "6 years",
                LocalDate.of(2022, 12, 17), new City(1, "Moscow"));
        store.add(post);
        post.setName("Java");
        store.update(post);
        Post postInDb = store.findById(post.getId());
        assertThat("Java", is(postInDb.getName()));
    }

    @Test
    public void findByIdPost() {
        Post post = new Post(0, "Java Job", "6 years",
                LocalDate.of(2022, 12, 17), new City(1, "Moscow"));
        store.add(post);
        Post postInDb = store.findById(post.getId());
        assertThat(post, is(postInDb));
    }

    @Test
    public void allPosts() {
        Post post1 = new Post(0, "Java Job", "1 years",
                LocalDate.of(2022, 12, 17), new City(1, "Moscow"));
        Post post2 = new Post(1, "Java Job", "2 years",
                LocalDate.of(2022, 12, 17), new City(1, "Moscow"));
        Post post3 = new Post(2, "Java Job", "3 years",
                LocalDate.of(2022, 12, 17), new City(1, "Moscow"));
        store.add(post1);
        store.add(post2);
        store.add(post3);
        List<Post> posts = List.of(post1, post2, post3);
        List<Post> postsI = store.findAll();
        assertThat(postsI, is(posts));
    }

    @AfterEach
    public void cleanTable() throws SQLException {
        try (Connection cn = POOL.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM post")) {
            ps.execute();
        }
    }
}