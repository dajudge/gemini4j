package org.gemini4j.cucumber;

import cucumber.api.PickleStepTestStep;
import cucumber.api.event.*;
import org.gemini4j.browser.Browser;
import org.gemini4j.core.Gemini4jConfiguration;
import org.gemini4j.core.Gemini4jContext;

import java.util.List;

import static java.util.ServiceLoader.load;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

public class Gemini4jPlugin implements EventListener {
    private static final Gemini4jContext<?> CONTEXT = new Gemini4jContext<>(findConfig());

    private static Gemini4jConfiguration<?> findConfig() {
        final List<Gemini4jConfiguration> configs = stream(load(Gemini4jConfiguration.class).spliterator(), false)
                .collect(toList());
        if (configs.isEmpty()) {
            throw new IllegalStateException("No configuration found");
        }
        if (configs.size() > 1) {
            throw new IllegalStateException("Multiple configurations found");
        }
        return configs.get(0);
    }

    public static <T> Browser<T> getBrowser(final Class<T> browserType) {
        return (Browser<T>) CONTEXT.getBrowser();
    }

    @Override
    public void setEventPublisher(final EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, event ->
                CONTEXT.getSnapper().nextTest(event.testCase.getName())
        );
        publisher.registerHandlerFor(TestStepFinished.class, event -> {
            final PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;
            CONTEXT.getSnapper().snap(testStep.getStepText());
        });
        publisher.registerHandlerFor(TestRunFinished.class, event -> CONTEXT.shutdown());
    }
}
