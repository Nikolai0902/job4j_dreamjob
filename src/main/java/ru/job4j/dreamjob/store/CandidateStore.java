package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CandidateStore {

    private static final CandidateStore INST = new CandidateStore();

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private CandidateStore() {
        candidates.put(1, new Candidate(1, "Nik", "student", LocalDate.now().plusDays(1)));
        candidates.put(2, new Candidate(2, "Ben", "3 year of experience", LocalDate.now().plusDays(2)));
        candidates.put(3, new Candidate(3, "Tom", "6 year of experience", LocalDate.now().plusDays(3)));
    }

    public static CandidateStore instOf() {
        return INST;
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
