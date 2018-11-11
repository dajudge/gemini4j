package org.gemini4j.core;

import org.gemini4j.browser.BrowserFactory;
import org.gemini4j.imageresolver.ReferenceImageResolverFactory;
import org.gemini4j.reporter.ReporterFactory;

public interface Gemini4jConfiguration<T> {
    BrowserFactory<T> getBrowserFactory();

    ReporterFactory getReporterFactory();

    ReferenceImageResolverFactory getReferenceImageResolverFactory();
}
