package org.unisens.test;

import org.junit.Test;
import org.unisens.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNull;
import static org.unisens.test.TestProperties.*;
import static org.unisens.test.TestUtils.copyDirectory;
import static org.unisens.test.TestUtils.deleteRecursive;

public class MultiThreading {
    private static final int NTHREDS = 50;
    private static List<AppendRunnable> workers = new LinkedList<AppendRunnable>();

    @Test
    public void testAppend() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
        for (int i = 0; i < 500; i++) {
            AppendRunnable worker = new AppendRunnable();
            workers.add(worker);
            executor.execute(worker);
        }
        // This will make the executor accept no new threads
        // and finish all existing threads in the queue
        executor.shutdown();
        // Wait until all threads are finish
        executor.awaitTermination(1, TimeUnit.MINUTES);
        for (AppendRunnable worker : workers) {
            assertNull(worker.getException());
        }
    }

    public class AppendRunnable implements Runnable {
        private Exception exception = null;

        @Override
        public void run() {
            File destination = new File(TEST_DEST + System.getProperty("file.separator") + UUID.randomUUID().toString() + System.getProperty("file.separator"));
            destination.mkdirs();
            try {
                copyDirectory(new File(TEST_SRC_BASE + System.getProperty("file.separator") + EXAMPLE2), destination);
                UnisensFactory factory = UnisensFactoryBuilder.createFactory();
                Unisens unisens = factory.createUnisens(destination.getAbsolutePath());
                ValuesEntry oldValuesEntry = (ValuesEntry) unisens.getEntry("bloodpressure.csv");
                Value v = new Value(1, (short)2);
                for (int i = 0; i < 10000; i++) {
                    oldValuesEntry.append(v);
                }
                unisens.closeAll();
            } catch (Exception e) {
                exception = e;
                e.printStackTrace();
            } finally {
                try {
                    deleteRecursive(destination);
                } catch (FileNotFoundException e) {
                    exception = e;
                    e.printStackTrace();
                }
            }
        }

        public Exception getException() {
            return exception;
        }
    }
}


