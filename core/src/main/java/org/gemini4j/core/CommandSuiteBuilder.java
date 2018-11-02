package org.gemini4j.core;

import org.gemini4j.api.Browser;
import org.gemini4j.plugins.BrowserFactory;
import org.gemini4j.utils.Clock;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Collections.unmodifiableList;
import static org.gemini4j.utils.UrlUtils.safeToUrl;

class CommandSuiteBuilder<B> implements SuiteBuilder<B> {
    private static final int WAIT_FOR_CHECK_INTERVAL = 100;

    private final List<Command<B>> commands = new ArrayList<>();
    private final BrowserFactory<B> browserFactory;
    private final ScreenshotProcessor screenshotProcessor;
    private final long defaultWaitForTimeout;
    private final Clock clock;

    CommandSuiteBuilder(
            final Clock clock,
            final BrowserFactory<B> browserFactory,
            final ScreenshotProcessor screenshotProcessor,
            final long defaultWaitForTimeout
    ) {
        this.clock = clock;
        this.browserFactory = browserFactory;
        this.screenshotProcessor = screenshotProcessor;
        this.defaultWaitForTimeout = defaultWaitForTimeout;
    }

    @Override
    public SuiteBuilder act(final Consumer<Browser<B>> interaction) {
        commands.add(interaction::accept);
        return this;
    }

    @Override
    public SuiteBuilder<B> waitFor(final Function<Browser<B>, Boolean> condition) {
        return waitFor(condition, defaultWaitForTimeout);
    }

    @Override
    public SuiteBuilder<B> waitFor(final Function<Browser<B>, Boolean> condition, long timeout) {
        final Command<B> waitForCommand = browser -> {
            final long start = clock.now();
            while ((clock.now() - start) < timeout) {
                if (condition.apply(browser)) {
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
        return act(browser -> screenshotProcessor.onImage(id, browser.takeScreenshot()));
    }

    @Override
    public Suite build() {
        return new CommandSuite<>(browserFactory, unmodifiableList(commands));
    }
}
