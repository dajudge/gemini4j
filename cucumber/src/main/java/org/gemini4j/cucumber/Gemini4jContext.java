package org.gemini4j.cucumber;

import org.gemini4j.core.Shite;
import org.gemini4j.reporter.Reporter;
import org.gemini4j.utils.Shutdown;

class Gemini4jContext implements Shutdown {

    private final Reporter reporter;
    private final Shite shite;

    Gemini4jContext(
            final ReporterFactory reporterFactory,
            final Shite.ReferenceImageResolver referenceImageResolver
    ) {
        this.reporter = reporterFactory.create();
        shite = new Shite(reporter, referenceImageResolver);
    }

    Shite getShite() {
        return shite;
    }

    @Override
    public void shutdown() {
        reporter.shutdown();
    }
}
