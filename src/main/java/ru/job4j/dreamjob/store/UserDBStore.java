package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class UserDBStore {

    private static final String INSERT = "INSERT INTO users(name, email, password) VALUES (?, ?, ?)";
    private static final String SELECT_EM_PASS = "SELECT * FROM users WHERE email = ? AND password = ?";

    private static final Logger LOG = LoggerFactory.getLogger(UserDBStore.class.getName());

    private final BasicDataSource pool;

    public UserDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<User> add(User user) {
        Optional<User> rsl = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
            rsl = Optional.of(user);
        } catch (Exception e) {
            LOG.error("Exception connection", e);
        }
        return rsl;
    }

    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        Optional<User> rsl = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_EM_PASS)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    rsl = Optional.of(newUser(it));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception connection", e);
        }
        return rsl;
    }

    private User newUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"));
    }
}
