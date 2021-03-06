package org.gemini4j.cucumber;

import org.gemini4j.browser.Browser;
import org.gemini4j.core.Gemini4jConfiguration;
import org.gemini4j.imageresolver.ClasspathReferenceImageResolver;
import org.gemini4j.imageresolver.ReferenceImageResolver;
import org.gemini4j.reporter.Reporter;
import org.gemini4j.selenium.WebDriverBrowser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Config implements Gemini4jConfiguration<WebDriver> {
    private static final String IMAGE_PREFIX = "org/gemini4j/cucumber/shots/";
    private static final ReferenceImageResolver IMAGE_RESOLVER = new ClasspathReferenceImageResolver(IMAGE_PREFIX);

    @Override
    public Reporter getReporter() {
        return CucumberTests.REPORTER;
    }

    @Override
    public ReferenceImageResolver getReferenceImageResolver() {
        return IMAGE_RESOLVER;
    }

    @Override
    public Browser<WebDriver> getBrowser() {
        return new WebDriverBrowser("http://localhost:4444/wd/hub", new ChromeOptions());
    }

}
