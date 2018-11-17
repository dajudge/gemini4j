package org.gemini4j.diesel;

import org.gemini4j.core.Gemini4jConfiguration;
import org.gemini4j.core.Gemini4jContext;
import org.gemini4j.utils.Clock;

public class Gemini4j {
    private static final Clock clock = Clock.create();
    private static final long DEFAULT_WAIT_FOR_TIMEOUT = 10000l;

    private Gemini4j() {

    }

    public static <B> SuiteBuilder<B> suite(final String suiteName, final Gemini4jConfiguration<B> config) {
        return new CommandSuiteBuilder<>(suiteName, clock, new Gemini4jContext<>(config), DEFAULT_WAIT_FOR_TIMEOUT);
    }
}
