package com.inventory;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class InventoryConcurrencyTest {

    @Test
    void concurrentReservationsShouldNotOversell() throws Exception {
        int requestCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(requestCount);

        CountDownLatch ready = new CountDownLatch(requestCount);
        CountDownLatch start = new CountDownLatch(1);

        AtomicInteger successes = new AtomicInteger();

        InventoryService service = new InventoryService(new ReservationRepository());

        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < requestCount; i++) {
            futures.add(executor.submit(() -> {
                try (Connection connection = DatabaseConnection.getConnection()) {
                    ready.countDown();
                    ;
                    start.await();
                    if (service.reserve(connection, 1)) {
                        successes.incrementAndGet();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));
        }

        ready.await();
        start.countDown();

        for (Future<?> future : futures) {
            future.get();
        }

        executor.shutdown();

        assertTrue(executor.awaitTermination(30, TimeUnit.SECONDS));

        assertEquals(10, successes.get());
    }
}
