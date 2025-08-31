package org.meeuw.math.demo;

import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.statistics.StatisticalLong;
import org.meeuw.time.UncertainJavaTime;

@Log4j2
class SolverTest {

    @Test
    void solve1() {
        Solver.SolverResult solve = Solver.solve(RationalNumbers.INSTANCE, "24", "8 8 3 3");
        List<String> list = solve.stream().toList();
        log.info("Solved {} -> {}", solve, list);
    }

    @Test
    void solvce2() {
        StatisticalLong duration =new StatisticalLong(UncertainJavaTime.Mode.DURATION);

        for (int i =0; i < 100; i++) {
            Instant start = Instant.now();
            Solver.SolverResult solve = Solver.solve(RationalNumbers.INSTANCE, "120", "4 7 7 7 8");
            List<String> list = solve.stream().toList();
            duration.enter(Duration.between(start, Instant.now()));
            if (i == 0) {
                log.info("Solved {} -> {}", solve, list);
            }
        }
        log.info("Solved: {}", duration.toString());
    }
}
