package org.gemini4j.utils;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ClockTest {
    private Clock subject = Clock.create();

    @Test
    public void returns_time() {
        assertTrue(subject.now() <= System.currentTimeMillis());
    }

    @Test
    public void waits() {
        long start = subject.now();
        subject.waitFor(100);
        long now = subject.now();
        assertTrue(now - start > 100);
    }
}
