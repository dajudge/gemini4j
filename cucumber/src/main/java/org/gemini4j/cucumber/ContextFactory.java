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
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.google.common.io.Files.write;
import static org.gemini4j.utils.NoExceptions.noex;

public class ContextFactory {

    private static final RemoteWebDriverBrowserFactory BROWSER_FACTORY = new RemoteWebDriverBrowserFactory(
            "http://localhost:4444/wd/hub",
            new ChromeOptions()
    );

    public Gemini4jContext<?> createContext() {
        final Consumer<byte[]> store = bytes -> noex(() -> write(bytes, new File("build/out.html")));
        final Supplier<InputStream> template = () -> getClass().getClassLoader()
                .getResourceAsStream("org/gemini4j/reporter/html/templates/standard.html");
        final ReporterFactory reporterFactory = () -> new HtmlReporter(store, template);
        final Shite.ReferenceImageResolver images = id -> {
            final String shot = "org/gemini4j/cucumber/shots/" + id + ".png";
            final InputStream is = getClass().getClassLoader().getResourceAsStream(shot);
            if (is == null) {
                System.out.println("No such screenshot: " + shot);
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
