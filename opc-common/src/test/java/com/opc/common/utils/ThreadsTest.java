package com.opc.common.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.*;

public class ThreadsTest
{
    @Test
    public void testSleep()
    {
        long startTime = System.currentTimeMillis();
        Threads.sleep(100);
        long endTime = System.currentTimeMillis();

        assertTrue(endTime - startTime >= 100);
    }

    @Test
    public void testSleepZero()
    {
        long startTime = System.currentTimeMillis();
        Threads.sleep(0);
        long endTime = System.currentTimeMillis();

        assertTrue(endTime - startTime < 50);
    }

    @Test
    public void testShutdownAndAwaitTermination()
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            Threads.sleep(50);
            return "done";
        });

        Threads.shutdownAndAwaitTermination(executor);

        assertTrue(executor.isShutdown());
        assertTrue(executor.isTerminated());
    }

    @Test
    public void testShutdownAndAwaitTerminationWithNull()
    {
        Threads.shutdownAndAwaitTermination(null);
    }

    @Test
    public void testShutdownAndAwaitTerminationAlreadyShutdown()
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.shutdown();

        Threads.shutdownAndAwaitTermination(executor);

        assertTrue(executor.isShutdown());
    }

    @Test
    public void testPrintExceptionWithRunnable()
    {
        Runnable runnable = () -> {
            throw new RuntimeException("Test exception");
        };

        assertDoesNotThrow(() -> {
            Threads.printException(runnable, new RuntimeException("Test"));
        });
    }

    @Test
    public void testPrintExceptionWithFuture() throws Exception
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<?> future = executor.submit(() -> {
            throw new RuntimeException("Test exception");
        });

        Threads.sleep(100);

        assertDoesNotThrow(() -> {
            Threads.printException((Runnable) future, null);
        });

        executor.shutdown();
    }

    @Test
    public void testPrintExceptionWithNull()
    {
        assertDoesNotThrow(() -> {
            Threads.printException(null, null);
        });
    }

    @Test
    public void testPrintExceptionWithThrowable()
    {
        Exception testException = new Exception("Test exception");

        assertDoesNotThrow(() -> {
            Threads.printException(null, testException);
        });
    }

    @Test
    public void testPrintExceptionWithCancellation() throws Exception
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> {
            Threads.sleep(1000);
            return "done";
        });

        future.cancel(true);
        Threads.sleep(100);

        assertDoesNotThrow(() -> {
            Threads.printException((Runnable) future, null);
        });

        executor.shutdown();
    }

    @Test
    public void testPrintExceptionWithInterruptedFuture() throws Exception
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> {
            Threads.sleep(10);
            return "done";
        });

        Threads.sleep(200);

        assertDoesNotThrow(() -> {
            Threads.printException((Runnable) future, null);
        });

        executor.shutdown();
    }
}
