package org.gemini4j.reporter.html;

import org.gemini4j.browser.Browser;
import org.gemini4j.core.Gemini4jConfiguration;
import org.gemini4j.imageresolver.ClasspathReferenceImageResolver;
import org.gemini4j.imageresolver.ReferenceImageResolver;
import org.gemini4j.reporter.Reporter;
import org.gemini4j.selenium.WebDriverBrowser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.function.BiConsumer;

public class Config implements Gemini4jConfiguration<WebDriver> {
    private BiConsumer<String, byte[]> store;
    private static final String IMAGE_PREFIX = "org/gemini4j/reporter/html";

    @Override
    public Reporter getReporter() {
        return new HtmlReporter(store, HtmlTemplate.STANDARD::openStream);
    }

    @Override
    public ReferenceImageResolver getReferenceImageResolver() {
        return new ClasspathReferenceImageResolver(IMAGE_PREFIX);
    }

    @Override
    public Browser<WebDriver> getBrowser() {
        return new WebDriverBrowser("http://localhost:4444/wd/hub", new ChromeOptions());
    }
}
