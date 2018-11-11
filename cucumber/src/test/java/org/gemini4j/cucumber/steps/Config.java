package org.gemini4j.cucumber.steps;

import org.gemini4j.browser.BrowserFactory;
import org.gemini4j.core.Gemini4jConfiguration;
import org.gemini4j.imageresolver.ReferenceImageResolver;
import org.gemini4j.imageresolver.ReferenceImageResolverFactory;
import org.gemini4j.reporter.ReporterFactory;
import org.gemini4j.reporter.html.HtmlReporter;
import org.gemini4j.selenium.RemoteWebDriverBrowserFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static org.gemini4j.utils.NoExceptions.noex;

public class Config implements Gemini4jConfiguration<WebDriver> {
    private static final RemoteWebDriverBrowserFactory BROWSER_FACTORY = new RemoteWebDriverBrowserFactory(
            "http://localhost:4444/wd/hub",
            new ChromeOptions()
    );
    private static final ReporterFactory REPORTER_FACTORY = () -> {
        final BiConsumer<String, byte[]> store = (fname, bytes) -> noex(() -> {
            final File file = new File("build/reports/tests/gemini4j/" + fname);
            file.getParentFile().mkdirs();
            Files.write(file.toPath(), bytes);
        });
        final Supplier<InputStream> template = () -> HtmlReporter.class.getClassLoader()
                .getResourceAsStream("org/gemini4j/reporter/html/templates/standard.html");
        return new HtmlReporter(store, template);
    };

    final ReferenceImageResolver IMAGE_RESOLVER = id -> {
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

    @Override
    public BrowserFactory<WebDriver> getBrowserFactory() {
        return BROWSER_FACTORY;
    }

    @Override
    public ReporterFactory getReporterFactory() {
        return REPORTER_FACTORY;
    }

    @Override
    public ReferenceImageResolverFactory getReferenceImageResolverFactory() {
        return () -> IMAGE_RESOLVER;
    }
}
