package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CandidateDBStore {

    private static final String SELECT = "SELECT * FROM candidate";
    private static final String INSERT = "INSERT INTO candidate(name, description, created, city_id) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE candidate set name = ?, description = ?, city_id = ? where id = ?";
    private static final String SELECT_W_ID = "SELECT * FROM candidate WHERE id = ?";

    private static final Logger LOG = LoggerFactory.getLogger(CandidateDBStore.class.getName());

    private final BasicDataSource pool;

    public CandidateDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT)) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(getCandidate(it));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception connection", e);
        }
        return candidates;
    }

    public Candidate add(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            candidate.setCreated(LocalDate.now());
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(candidate.getCreated().atStartOfDay()));
            ps.setInt(4, candidate.getCity().getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception connection", e);
        }
        return candidate;
    }

    public void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE)) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDescription());
            ps.setInt(3, candidate.getCity().getId());
            ps.setInt(4, candidate.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            LOG.error("Exception connection", e);
        }
    }

    public Candidate findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_W_ID)) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return getCandidate(it);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception connection", e);
        }
        return null;
    }

    private Candidate getCandidate(ResultSet it) throws SQLException {
        return new Candidate(it.getInt("id"), it.getString("name"),
                it.getString("description"), it.getTimestamp("created").toLocalDateTime().toLocalDate(),
                new City(it.getInt("city_id"), ""));
    }
}
