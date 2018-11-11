package org.gemini4j.cucumber;

import cucumber.api.PickleStepTestStep;
import cucumber.api.event.*;
import org.gemini4j.api.Browser;

public class Gemini4jPlugin implements EventListener {
    private final Gemini4jContext context = new ContextFactory().createContext();
    private static Browser<?> browser;

    private static <T> Browser<T> getOrCreateBrowser(Gemini4jConfiguration<T> config) {
        if (browser == null) {
            browser = config.getBrowserFactory().create();
        }
        return (Browser<T>) browser;
    }

    private void shutdownContextIfExists() {
        if (browser != null) {
            browser.shutdown();
        }
        context.shutdown();
    }

    public static <T> Browser<T> getBrowser(Gemini4jConfiguration<T> config) {
        return getOrCreateBrowser(config);
    }

    @Override
    public void setEventPublisher(final EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, event ->
                context.getShite().nextTest(event.testCase.getName())
        );
        publisher.registerHandlerFor(TestStepFinished.class, event -> {
            final PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;
            if (browser != null) {
                context.getShite().snap(browser, testStep.getStepText());
            }
        });
        publisher.registerHandlerFor(TestRunFinished.class, event -> shutdownContextIfExists());
    }
}
