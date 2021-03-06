package org.gemini4j.diesel;

import org.gemini4j.browser.Browser;

import java.net.URL;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface SuiteBuilder<B> {
    SuiteBuilder<B> url(URL url);

    SuiteBuilder<B> url(String s);

    SuiteBuilder<B> snap(String id);

    SuiteBuilder<B> act(Consumer<Browser<B>> interaction);

    SuiteBuilder<B> waitFor(Predicate<Browser<B>> condition);

    SuiteBuilder<B> waitFor(Predicate<Browser<B>> condition, long timeout);

    Suite build();
}
