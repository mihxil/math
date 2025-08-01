package org.meeuw.math.test;

import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;

@Log4j2
class SolverTest {

    @Test
    void solve1() {
        Solver.SolverResult solve = Solver.solve(RationalNumbers.INSTANCE, "24", "8 8 3 3");
        List<String> list = solve.stream().toList();
        log.info("Solved {} -> {}", solve, list);
    }

    @Test
    void solve2() {
        Instant start = Instant.now();
        Solver.SolverResult solve = Solver.solve(RationalNumbers.INSTANCE, "120", "4 7 7 7 8");
        List<String> list = solve.stream().toList();
        log.info("Solved {} -> {} ({})", solve, list, Duration.between(start, Instant.now()));
    }
}
