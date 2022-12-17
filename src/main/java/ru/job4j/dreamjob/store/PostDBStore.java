package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostDBStore {

    private static final String SELECT = "SELECT * FROM post";
    private static final String INSERT = "INSERT INTO post(name, description, created, city_id) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE post set name = ?, description = ?, city_id = ? where id = ?";
    private static final String SELECT_W_ID = "SELECT * FROM post WHERE id = ?";

    private static final Logger LOG = LoggerFactory.getLogger(PostDBStore.class.getName());

    private final BasicDataSource pool;

    public PostDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT)) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name"),
                            it.getString("description"), it.getTimestamp("created").toLocalDateTime().toLocalDate(),
                            new City(it.getInt("city_id"), "")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception connection", e);
        }
        return posts;
    }

    public Post add(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            post.setCreated(LocalDate.now());
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(post.getCreated().atStartOfDay()));
            ps.setInt(4, post.getCity().getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception connection", e);
        }
        return post;
    }

    public void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setInt(3, post.getCity().getId());
            ps.setInt(4, post.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.error("Exception connection", e);
        }
    }

    public Post findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_W_ID)) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return getPost(it);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception connection", e);
        }
        return null;
    }

    private Post getPost(ResultSet it) throws SQLException {
        return new Post(it.getInt("id"), it.getString("name"),
                it.getString("description"), it.getObject(4, LocalDate.class),
                new City(it.getInt("city_id"), ""));
    }
}
