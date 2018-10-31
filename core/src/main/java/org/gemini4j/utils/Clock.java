package org.gemini4j.utils;

public interface Clock {
    public static Clock create() {
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
                    e.printStackTrace();
                }
            }
        };
    }

    public long now();

    public void waitFor(long msecs);
}
