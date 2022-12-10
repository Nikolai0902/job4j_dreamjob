package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class CandidateStore {

    private static AtomicInteger id = new AtomicInteger(4);
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private CandidateStore() {
        candidates.put(1, new Candidate(1, "Nik", "student", LocalDate.now().plusDays(1), null));
        candidates.put(2, new Candidate(2, "Ben", "3 year of experience", LocalDate.now().plusDays(2), null));
        candidates.put(3, new Candidate(3, "Tom", "6 year of experience", LocalDate.now().plusDays(3), null));
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    public void add(Candidate candidate) {
        candidate.setId(id.getAndIncrement());
        candidate.setCreated(LocalDate.now());
        candidates.put(candidate.getId(), candidate);
    }

    public Object findById(int id) {
        return candidates.get(id);
    }

    public void update(Candidate candidate) {
        candidates.put(candidate.getId(), candidate);
    }
}
