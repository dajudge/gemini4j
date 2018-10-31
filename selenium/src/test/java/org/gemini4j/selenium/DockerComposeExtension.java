package org.gemini4j.selenium;

import com.palantir.docker.compose.DockerComposeRule;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Field;
import java.util.Set;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;

public class DockerComposeExtension implements BeforeAllCallback, AfterAllCallback {
    private DockerComposeRule docker;

    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        extensionContext.getTestClass().ifPresent(clazz -> {
            final Set<Field> candidates = of(clazz.getDeclaredFields())
                    .filter(it -> isStatic(it.getModifiers()))
                    .filter(it -> it.getType() == DockerComposeRule.class)
                    .collect(toSet());
            if (candidates.isEmpty()) {
                return;
            }
            if (candidates.size() > 1) {
                throw new IllegalStateException("Multiple docker compose rules defined.");
            }
            final Field field = candidates.iterator().next();
            field.setAccessible(true);
            try {
                docker = (DockerComposeRule) field.get(null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access " + field, e);
            }
        });
        if (docker != null) {
            docker.before();
        }
    }

    public void afterAll(ExtensionContext extensionContext) throws Exception {
        if (docker != null) {
            docker.after();
        }
    }

}
