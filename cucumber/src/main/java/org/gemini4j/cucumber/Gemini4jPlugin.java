package org.gemini4j.cucumber;

import cucumber.api.PickleStepTestStep;
import cucumber.api.event.*;
import org.gemini4j.api.Browser;
import org.openqa.selenium.WebDriver;

public class Gemini4jPlugin implements EventListener {
    private static ContextFactory CONTEXT_FACTORY = new ContextFactory();
    private static ThreadLocal<Gemini4jContext<?>> THREAD_CONTEXT = new ThreadLocal<>();

    private static synchronized <T> Gemini4jContext<T> getOrCreateContext(final Class<T> implementationClass) {
        if (THREAD_CONTEXT.get() == null) {
            THREAD_CONTEXT.set(CONTEXT_FACTORY.createContext(implementationClass));
        }
        return (Gemini4jContext<T>) THREAD_CONTEXT.get();
    }

    private static void shutdownContextIfExists() {
        if (THREAD_CONTEXT != null) {
            THREAD_CONTEXT.get().shutdown();
            THREAD_CONTEXT.remove();
        }
    }

    public static <T> Browser<T> getBrowser(Class<T> implementationClass) {
        return getOrCreateContext(implementationClass).getBrowser();
    }

    @Override
    public void setEventPublisher(final EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, event -> {
            getOrCreateContext(WebDriver.class).getShite().nextTest(event.testCase.getName());
        });
        publisher.registerHandlerFor(TestStepFinished.class, event -> {
            final PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;
            getOrCreateContext(WebDriver.class).getShite().snap(testStep.getStepText());
        });
        publisher.registerHandlerFor(TestRunFinished.class, event -> shutdownContextIfExists());
    }
}
