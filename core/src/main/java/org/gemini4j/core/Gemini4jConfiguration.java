package org.gemini4j.core;

import org.gemini4j.browser.Browser;
import org.gemini4j.browser.BrowserFactory;
import org.gemini4j.imageresolver.ReferenceImageResolver;
import org.gemini4j.reporter.Reporter;

public interface Gemini4jConfiguration<T> {
    Reporter getReporter();

    ReferenceImageResolver getReferenceImageResolver();

    Browser<T> getBrowser();
}
