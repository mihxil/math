/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.test.math;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import lombok.SneakyThrows;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.meeuw.math.CollectionUtils;

public class CollectionUtilsTest {

    @Test
    public void memoize() throws ExecutionException, InterruptedException {
        Supplier<Integer> sup = new Supplier<Integer>() {
            AtomicInteger i = new AtomicInteger(0);
            @SneakyThrows
            @Override
            public Integer get() {
                Thread.sleep(100);
                return i.incrementAndGet();
            }
        };

        Supplier<Integer> memoize = CollectionUtils.memoize(sup);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<Future<Integer>> f = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            f.add(executor.submit(new Callable<Integer>() {
                @Override
                public Integer call() {
                    return memoize.get();
                }
            }));
        }
        for (Future<Integer> fut : f) {
            assertThat(fut.get()).isEqualTo(1);
        }
        executor.shutdown();
    }
}
