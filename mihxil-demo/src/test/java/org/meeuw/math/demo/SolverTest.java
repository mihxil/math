package org.meeuw.math.demo;

import lombok.extern.java.Log;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.arithmetic.ast.Expression;
import org.meeuw.math.statistics.StatisticalLong;
import org.meeuw.time.UncertainJavaTime;

@Log
class SolverTest {

    @Test
    void solve1() {
        Solver<RationalNumber> solver = new Solver<>(RationalNumbers.INSTANCE) {
            @Override
            void callBack(long considered, long current, long total, Expression<RationalNumber> expression) {

            }
        };
        Solver.SolverResult solve = solver.solve("24", "8 8 3 3");
        List<String> list = solve.stream().toList();
        log.info(() -> "Solved %s -> %s".formatted(solve, list));
    }

    @Test
    void solve2() {
        StatisticalLong duration =new StatisticalLong(UncertainJavaTime.Mode.DURATION);
        Solver<RationalNumber> solver = new Solver<>(RationalNumbers.INSTANCE);
        for (int i = 0; i < 100; i++) {
            Instant start = Instant.now();
            Solver.SolverResult solve = solver.solve("120", "4 7 7 7 8");
            List<String> list = solve.stream().toList();
            duration.enter(Duration.between(start, Instant.now()));
            if (i == 0) {
                log.info("Solved %s ->%s".formatted(solve, list));
            }
        }
        log.info("Solved: %s".formatted(duration.toString()));
    }
}
