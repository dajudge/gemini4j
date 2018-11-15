package org.gemini4j.cucumber;

import org.gemini4j.browser.BrowserFactory;
import org.gemini4j.core.Gemini4jConfiguration;
import org.gemini4j.imageresolver.ReferenceImageResolver;
import org.gemini4j.imageresolver.ReferenceImageResolverFactory;
import org.gemini4j.reporter.ReporterFactory;
import org.gemini4j.selenium.RemoteWebDriverBrowserFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class Config implements Gemini4jConfiguration<WebDriver> {
    private static final RemoteWebDriverBrowserFactory BROWSER_FACTORY = new RemoteWebDriverBrowserFactory(
            "http://localhost:4444/wd/hub",
            new ChromeOptions()
    );

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
        return () -> CucumberTests.REPORTER;
    }

    @Override
    public ReferenceImageResolverFactory getReferenceImageResolverFactory() {
        return () -> IMAGE_RESOLVER;
    }
}
