package org.gemini4j.core;

import org.gemini4j.browser.Browser;
import org.gemini4j.imageresolver.ReferenceImageResolver;
import org.gemini4j.reporter.Reporter;
import org.gemini4j.utils.Shutdown;

public class Gemini4jContext<T> implements Shutdown {
    private final Gemini4jConfiguration<T> config;

    private Snapper<T> snapper;
    private Reporter reporter;
    private Browser<T> browser;
    private ReferenceImageResolver images;

    public Gemini4jContext(final Gemini4jConfiguration<T> config) {
        this.config = config;
    }

    public Snapper<T> getSnapper() {
        ensureInitialized();
        return snapper;
    }

    private void ensureInitialized() {
        if (snapper == null) {
            reporter = config.getReporterFactory().create();
            browser = config.getBrowserFactory().create();
            images = config.getReferenceImageResolverFactory().create();
            snapper = new Snapper(reporter, images, browser);
        }
    }

    @Override
    public void shutdown() {
        if (reporter != null) {
            reporter.shutdown();
        }
        if (browser != null) {
            browser.shutdown();
        }
    }

    public Browser<T> getBrowser() {
        ensureInitialized();
        return browser;
    }
}
