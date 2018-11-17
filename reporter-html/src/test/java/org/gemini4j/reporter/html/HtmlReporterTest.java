package org.gemini4j.reporter.html;

import com.palantir.docker.compose.DockerComposeRule;
import org.gemini4j.diesel.Gemini4j;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.gemini4j.reporter.html.HtmlTemplate.STANDARD;
import static org.gemini4j.testapp.util.TestAppUtil.testAppEnvironment;
import static org.gemini4j.testapp.util.TestAppUtil.uploadStatusResources;

public class HtmlReporterTest {
    @ClassRule
    public static final DockerComposeRule DOCKER = testAppEnvironment();

    @Test
    public void does_something() throws IOException {
        uploadReport(STANDARD, reporter -> {
            reporter.nextTest("Next Test");
        });
        Gemini4j.suite("SuiteName", new Config());
    }

    private void uploadReport(final HtmlTemplate templatePath, final Consumer<HtmlReporter> test) throws IOException {
        uploadStatusResources(cb -> {
            final BiConsumer<String, byte[]> store = (s, bytes) -> {
                try {
                    cb.onFile("static/" + s, bytes.length, new ByteArrayInputStream(bytes));
                } catch (final IOException e) {
                    throw new RuntimeException("Failed to store report resource " + s, e);
                }
            };
            final HtmlReporter reporter = new HtmlReporter(store, templatePath::openStream);
            test.accept(reporter);
            reporter.shutdown();
        });
    }
}
