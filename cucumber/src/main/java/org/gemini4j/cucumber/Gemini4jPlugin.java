package org.gemini4j.cucumber;

import cucumber.api.PickleStepTestStep;
import cucumber.api.event.*;
import org.gemini4j.core.Browser;

public class Gemini4jPlugin implements EventListener {
    private static ContextFactory CONTEXT_FACTORY = new ContextFactory();
    private static ThreadLocal<Gemini4jContext<?>> THREAD_CONTEXT = new ThreadLocal<>();

    private static synchronized Gemini4jContext<?> getOrCreateContext() {
        if (THREAD_CONTEXT.get() == null) {
            THREAD_CONTEXT.set(CONTEXT_FACTORY.createContext());
        }
        return THREAD_CONTEXT.get();
    }

    private static void shutdownContextIfExists() {
        if (THREAD_CONTEXT != null) {
            THREAD_CONTEXT.get().shutdown();
            THREAD_CONTEXT.remove();
        }
    }

    public static <T> Browser<T> getBrowser(Class<T> implementationClass) {
        return (Browser<T>) getOrCreateContext().getBrowser();
    }

    @Override
    public void setEventPublisher(final EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, event -> {
            getOrCreateContext().getShite().nextTest(event.testCase.getName());
        });
        publisher.registerHandlerFor(TestStepFinished.class, event -> {
            final PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;
            getOrCreateContext().getShite().snap(testStep.getStepText());
        });
        publisher.registerHandlerFor(TestRunFinished.class, event -> shutdownContextIfExists());
    }
}
