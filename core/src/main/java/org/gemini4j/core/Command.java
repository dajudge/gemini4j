package org.gemini4j.core;

interface Command<B> {
    void execute(Browser<B> browser);
}
