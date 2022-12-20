package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.config.JdbcConfiguration;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class CandidateDBStoreTest {

    private final static BasicDataSource POOL = new JdbcConfiguration().loadPool();
    CandidateDBStore store = new CandidateDBStore(POOL);

    @Test
    public void whenCreateCandidate() {
        Candidate candidate = new Candidate(0, "Nik", "6 years",
                LocalDate.of(2022, 12, 17), new City(1, "Moscow"), null);
        store.add(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidate.getName()));
    }

    @Test
    public void whenUpdateCandidate() {
        Candidate candidate = new Candidate(0, "Nik", "6 years",
                LocalDate.of(2022, 12, 17), new City(1, "Moscow"), null);
        store.add(candidate);
        candidate.setName("Ben");
        store.update(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        assertThat("Ben", is(candidateInDb.getName()));
    }

    @Test
    public void findByIdCandidate() {
        Candidate candidate = new Candidate(0, "Nik", "6 years",
                LocalDate.of(2022, 12, 17), new City(1, "Moscow"), null);
        store.add(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        assertThat(candidate, is(candidateInDb));
    }

    @Test
    public void allPosts() {
        Candidate candidate1 = new Candidate(0, "Nik", "6 years",
                LocalDate.of(2022, 12, 17), new City(1, "Moscow"), null);
        Candidate candidate2 = new Candidate(1, "Ben", "6 years",
                LocalDate.of(2022, 12, 17), new City(1, "Moscow"), null);
        Candidate candidate3 = new Candidate(2, "Tom", "6 years",
                LocalDate.of(2022, 12, 17), new City(1, "Moscow"), null);
        store.add(candidate1);
        store.add(candidate2);
        store.add(candidate3);
        List<Candidate> candidates = List.of(candidate1, candidate2, candidate3);
        List<Candidate> candidatesI = store.findAll();
        assertThat(candidatesI, is(candidates));
    }

    @AfterEach
    public void cleanTable() throws SQLException {
        try (Connection cn = POOL.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM candidate")) {
            ps.execute();
        }
    }
}