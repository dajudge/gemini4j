package org.gemini4j.diesel;

import org.gemini4j.browser.Browser;
import org.gemini4j.core.Gemini4jContext;
import org.gemini4j.utils.Clock;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.util.Collections.unmodifiableList;
import static org.gemini4j.utils.UrlUtils.safeToUrl;

class CommandSuiteBuilder<B> implements SuiteBuilder<B> {
    private static final int WAIT_FOR_CHECK_INTERVAL = 100;

    private final List<Command<B>> commands = new ArrayList<>();
    private final Gemini4jContext<B> context;
    private final long defaultWaitForTimeout;
    private final String suiteName;
    private final Clock clock;

    CommandSuiteBuilder(
            final String suiteName,
            final Clock clock,
            final Gemini4jContext<B> context,
            final long defaultWaitForTimeout
    ) {
        this.suiteName = suiteName;
        this.clock = clock;
        this.context = context;
        this.defaultWaitForTimeout = defaultWaitForTimeout;
    }

    @Override
    public SuiteBuilder act(final Consumer<Browser<B>> interaction) {
        commands.add(interaction::accept);
        return this;
    }

    @Override
    public SuiteBuilder<B> waitFor(final Predicate<Browser<B>> condition) {
        return waitFor(condition, defaultWaitForTimeout);
    }

    @Override
    public SuiteBuilder<B> waitFor(final Predicate<Browser<B>> condition, long timeout) {
        final Command<B> waitForCommand = browser -> {
            final long start = clock.now();
            while ((clock.now() - start) < timeout) {
                if (condition.test(browser)) {
                    return;
                }
                clock.waitFor(WAIT_FOR_CHECK_INTERVAL);
            }
            throw new AssertionError("Condition did not become true in time: " + condition);
        };
        commands.add(waitForCommand);
        return this;
    }

    @Override
    public SuiteBuilder url(final URL url) {
        return act(browser -> browser.navigateTo(url));
    }

    @Override
    public SuiteBuilder<B> url(final String url) {
        return url(safeToUrl(url));
    }

    @Override
    public SuiteBuilder snap(final String id) {
        return act(browser -> context.getSnapper().snap(id));
    }

    @Override
    public Suite build() {
        return new CommandSuite<>(suiteName, context, unmodifiableList(commands));
    }
}
