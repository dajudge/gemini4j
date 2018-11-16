package org.gemini4j.reporter.html;

import com.palantir.docker.compose.DockerComposeRule;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.gemini4j.testapp.TestAppUtil.testAppEnvironment;
import static org.gemini4j.testapp.TestAppUtil.uploadStatusResources;

public class HtmlReporterTest {
    @ClassRule
    public static final DockerComposeRule DOCKER = testAppEnvironment();
    public static final String STANDARD_TEMPLATE = "org/gemini4j/reporter/html/templates/standard.html";

    @Test
    public void does_something() throws IOException {
        uploadReport(STANDARD_TEMPLATE, reporter -> {
            reporter.nextTest("Next Test");
        });
    }

    private void uploadReport(final String templatePath, final Consumer<HtmlReporter> test) throws IOException {
        uploadStatusResources(cb -> {
            final BiConsumer<String, byte[]> store = (s, bytes) -> {
                try {
                    cb.onFile("static/" + s, bytes.length, new ByteArrayInputStream(bytes));
                } catch (final IOException e) {
                    throw new RuntimeException("Failed to store report resource " + s, e);
                }
            };
            final Supplier<InputStream> template = () -> HtmlReporter.class.getClassLoader()
                    .getResourceAsStream(templatePath);
            final HtmlReporter reporter = new HtmlReporter(store, template);
            test.accept(reporter);
            reporter.shutdown();
        });
    }
}
