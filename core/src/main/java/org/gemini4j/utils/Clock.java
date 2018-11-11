package org.gemini4j.utils;

public interface Clock {
    static Clock create() {
        return new Clock() {

            @Override
            public long now() {
                return System.currentTimeMillis();
            }

            @Override
            public void waitFor(final long msecs) {
                try {
                    Thread.sleep(msecs);
                } catch (final InterruptedException e) {
                    throw new RuntimeException("Interrupted while waiting", e);
                }
            }
        };
    }

    long now();

    void waitFor(long msecs);
}
