package org.gemini4j.cucumber;

import org.gemini4j.core.Shite;
import org.gemini4j.reporter.html.HtmlReporter;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static org.gemini4j.utils.NoExceptions.noex;

public class ContextFactory {
    public Gemini4jContext createContext() {
        final BiConsumer<String, byte[]> store = (fname, bytes) -> noex(() -> {
            final File file = new File("build/reports/tests/gemini4j/" + fname);
            file.getParentFile().mkdirs();
            Files.write(file.toPath(), bytes);
        });
        final Supplier<InputStream> template = () -> getClass().getClassLoader()
                .getResourceAsStream("org/gemini4j/reporter/html/templates/standard.html");
        final ReporterFactory reporterFactory = () -> new HtmlReporter(store, template);
        final Shite.ReferenceImageResolver images = id -> {
            final String shot = "org/gemini4j/cucumber/shots/" + id + ".png";
            final InputStream is = getClass().getClassLoader().getResourceAsStream(shot);
            if (is == null) {
                return Optional.empty();
            }
            try {
                return Optional.of(ImageIO.read(is));
            } catch (IOException e) {
                throw new RuntimeException("Failed to load shot", e);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    // oh please
                }
            }
        };
        return new Gemini4jContext(reporterFactory, images);
    }
}
