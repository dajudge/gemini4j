package org.gemini4j.utils;

public class NoExceptions {
    public static <T> T noex(final ThrowingSupplier<T> o) {
        try {
            return o.get();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void noex(final ThrowingRunnable r) {
        try {
            r.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public interface ThrowingSupplier<T> {
        T get() throws Exception;
    }

    public interface ThrowingRunnable {
        void run() throws Exception;
    }
}
