package org.gemini4j.selenium;

import org.gemini4j.browser.Browser;
import org.gemini4j.browser.BrowserFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

import static org.gemini4j.utils.UrlUtils.safeToUrl;

public class RemoteWebDriverBrowserFactory implements BrowserFactory<WebDriver> {
    private final URL remoteAddress;
    private final Capabilities capabilities;

    public RemoteWebDriverBrowserFactory(final URL remoteAddress, final Capabilities capabilities) {
        this.remoteAddress = remoteAddress;
        this.capabilities = capabilities;
    }

    public RemoteWebDriverBrowserFactory(final String remoteAddress, final Capabilities capabilities) {
        this(safeToUrl(remoteAddress), capabilities);
    }

    @Override
    public Browser<WebDriver> create() {
        final RemoteWebDriver driver = new RemoteWebDriver(remoteAddress, capabilities);
        return new SeleniumBrowser(driver);
    }
}
