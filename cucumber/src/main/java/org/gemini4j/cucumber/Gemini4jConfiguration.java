package org.gemini4j.cucumber;

import org.gemini4j.plugins.BrowserFactory;

public interface Gemini4jConfiguration<T> {
    BrowserFactory<T> getBrowserFactory();
}
