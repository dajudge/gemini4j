package org.gemini4j.cucumber;

import cucumber.api.PickleStepTestStep;
import cucumber.api.event.*;
import org.gemini4j.browser.Browser;
import org.gemini4j.core.Gemini4jConfiguration;
import org.gemini4j.core.Gemini4jContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
            if (!skipSnap(testStep)) {
                CONTEXT.getSnapper().snap(testStep.getStepText());
            }
        });
        publisher.registerHandlerFor(TestRunFinished.class, event -> CONTEXT.shutdown());
    }

    private boolean skipSnap(final PickleStepTestStep testStep) {
        try {
            return !isAnnotatedWith(testStep, DontSnap.class);
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }

    private boolean isAnnotatedWith(
            final PickleStepTestStep testStep,
            final Class<? extends Annotation> annotation
    ) throws NoSuchFieldException, IllegalAccessException {
        final Method method = getStepMethod(testStep);
        if (method.isAnnotationPresent(annotation)) {
            return true;
        }
        if (method.getDeclaringClass().isAnnotationPresent(annotation)) {
            return true;
        }
        return false;
    }

    private Method getStepMethod(final PickleStepTestStep testStep) throws NoSuchFieldException, IllegalAccessException {
        final Field definitionMatchField = testStep.getClass().getDeclaredField("definitionMatch");
        definitionMatchField.setAccessible(true);
        final Object definitionMatch = definitionMatchField.get(testStep);
        final Field stepDefinitionField = definitionMatch.getClass().getDeclaredField("stepDefinition");
        stepDefinitionField.setAccessible(true);
        final Object stepDefinition = stepDefinitionField.get(definitionMatch);
        final Field methodField = stepDefinition.getClass().getDeclaredField("method");
        methodField.setAccessible(true);
        return (Method) methodField.get(stepDefinition);
    }
}
