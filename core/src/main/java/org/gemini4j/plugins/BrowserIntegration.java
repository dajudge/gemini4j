package org.gemini4j.plugins;

import java.util.Optional;

public interface BrowserIntegration {
    <T> Optional<BrowserFactory<T>> createIntegration(Class<T> integrationType);
}
