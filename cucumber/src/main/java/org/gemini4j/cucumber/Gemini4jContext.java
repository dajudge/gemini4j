package org.gemini4j.cucumber;

import org.gemini4j.core.Browser;
import org.gemini4j.core.BrowserFactory;
import org.gemini4j.core.Shite;
import org.gemini4j.reporter.Reporter;
import org.gemini4j.utils.Shutdown;

class Gemini4jContext<T> implements Shutdown {

    private final Reporter reporter;
    private final Browser<T> browser;
    private final Shite shite;

    Gemini4jContext(
            final ReporterFactory reporterFactory,
            final BrowserFactory<T> browserFactory,
            final Shite.ReferenceImageResolver referenceImageResolver
    ) {
        this.reporter = reporterFactory.create();
        this.browser = browserFactory.create();
        shite = new Shite(browser, reporter, referenceImageResolver);
    }

    Shite getShite() {
        return shite;
    }

    @Override
    public void shutdown() {
        browser.shutdown();
        reporter.shutdown();
    }

    Browser<T> getBrowser() {
        return browser;
    }
}
