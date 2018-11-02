package org.gemini4j.selenium;

import org.gemini4j.plugins.BrowserFactory;
import org.gemini4j.plugins.BrowserIntegration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Optional;

public class SeleniumBrowserIntegration implements BrowserIntegration {
    private static final RemoteWebDriverBrowserFactory BROWSER_FACTORY = new RemoteWebDriverBrowserFactory(
            "http://localhost:4444/wd/hub",
            new ChromeOptions()
    );

    @Override
    public <T> Optional<BrowserFactory<T>> createIntegration(final Class<T> integrationType) {
        if (integrationType != WebDriver.class) {
            return Optional.empty();
        }
        return Optional.of((BrowserFactory<T>) (BrowserFactory<WebDriver>) () -> BROWSER_FACTORY.create());
    }
}
