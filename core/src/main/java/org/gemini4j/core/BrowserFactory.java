package org.gemini4j.core;

public interface BrowserFactory<B> {
    Browser<B> create();
}
