package org.gemini4j.core;

import java.net.URL;
import java.util.function.Consumer;
import java.util.function.Function;

public interface SuiteBuilder<B> {
    SuiteBuilder<B> url(URL url);

    SuiteBuilder<B> url(String s);

    SuiteBuilder<B> snap(String id);

    SuiteBuilder<B> act(Consumer<Browser<B>> interaction);

    SuiteBuilder<B> waitFor(Function<Browser<B>, Boolean> condition);

    SuiteBuilder<B> waitFor(Function<Browser<B>, Boolean> condition, long timeout);

    Suite build();
}
