package org.gemini4j.cucumber;

import org.gemini4j.plugins.BrowserFactory;
import org.gemini4j.plugins.BrowserIntegration;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.StreamSupport.stream;

class BrowserIntegrationDiscovery {
    static final BrowserIntegrationDiscovery INSTANCE = new BrowserIntegrationDiscovery();

    private final ServiceLoader<BrowserIntegration> loader = ServiceLoader.load(BrowserIntegration.class);

    <T> BrowserFactory<T> findFactoryFor(Class<T> integrationType) {
        final Set<Optional<BrowserFactory<T>>> integrations = stream(loader.spliterator(), false)
                .map(it -> it.createIntegration(integrationType))
                .filter(Optional::isPresent)
                .collect(Collectors.toSet());
        if (integrations.isEmpty()) {
            throw new IllegalStateException("No gemini4j browser integration for "
                    + integrationType.getName()
                    + " found");
        }
        if (integrations.size() > 1) {
            throw new IllegalStateException("Found ambiguous browser integrations for "
                    + integrationType.getName()
                    + ": "
                    + integrations);
        }
        return integrations.iterator().next().get();
    }
}
