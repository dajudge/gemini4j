package org.gemini4j.core;

import org.gemini4j.api.Browser;

interface Command<B> {
    void execute(Browser<B> browser);
}
