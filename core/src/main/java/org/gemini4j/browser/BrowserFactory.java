package org.gemini4j.browser;

public interface BrowserFactory<B> {
    Browser<B> createBrowser();
}
