package org.gemini4j.plugins;

import org.gemini4j.api.Browser;

public interface BrowserFactory<B> {
    Browser<B> create();
}
