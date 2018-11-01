package org.gemini4j.cucumber;

import org.gemini4j.core.Shite;
import org.gemini4j.reporter.html.HtmlReporter;
import org.gemini4j.selenium.RemoteWebDriverBrowserFactory;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static com.google.common.io.Files.write;
import static org.gemini4j.utils.NoExceptions.noex;

public class ContextFactory {

    private static final RemoteWebDriverBrowserFactory BROWSER_FACTORY = new RemoteWebDriverBrowserFactory(
            "http://localhost:4444/wd/hub",
            new ChromeOptions()
    );

    public Gemini4jContext<?> createContext() {
        final BiConsumer<String, byte[]> store = (fname, bytes) -> noex(() -> {
            final File file = new File("build/reports/tests/gemini4j/" + fname);
            file.getParentFile().mkdirs();
            write(bytes, file);
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
        return new Gemini4jContext<>(reporterFactory, BROWSER_FACTORY, images);
    }
}
