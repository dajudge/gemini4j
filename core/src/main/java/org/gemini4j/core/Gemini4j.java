package org.gemini4j.core;

import org.gemini4j.utils.Clock;

public class Gemini4j {
    private static final Clock clock = Clock.create();
    private static final long DEFAULT_WAIT_FOR_TIMEOUT = 10000l;

    public static <B> SuiteBuilder<B> suite(
            final BrowserFactory<B> browserFactory,
            final ScreenshotProcessor screenshotProcessor
    ) {
        return new CommandSuiteBuilder<>(clock, browserFactory, screenshotProcessor, DEFAULT_WAIT_FOR_TIMEOUT);
    }
}