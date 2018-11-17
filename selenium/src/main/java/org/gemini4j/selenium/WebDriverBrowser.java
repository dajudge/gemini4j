package org.gemini4j.selenium;

import org.gemini4j.browser.Browser;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import static org.gemini4j.utils.UrlUtils.safeToUrl;

public class WebDriverBrowser implements Browser<WebDriver> {
    private WebDriver webDriver;

    public WebDriverBrowser(final URL remoteAddress, final Capabilities capabilities) {
        this(new RemoteWebDriver(remoteAddress, capabilities));

    }

    public WebDriverBrowser(final String remoteAddress, final Capabilities capabilities) {
        this(safeToUrl(remoteAddress), capabilities);
    }

    public WebDriverBrowser(final WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public void navigateTo(final URL url) {
        webDriver.navigate().to(url);
    }

    @Override
    public void shutdown() {
        webDriver.quit();
    }

    public WebDriver delegate() {
        return webDriver;
    }

    @Override
    public BufferedImage takeScreenshot() {
        final TakesScreenshot takesScreenshot = (TakesScreenshot) webDriver;
        final byte[] imageData = takesScreenshot.getScreenshotAs(OutputType.BYTES);
        try {
            return ImageIO.read(new ByteArrayInputStream(imageData));
        } catch (final IOException e) {
            throw new RuntimeException("Failed to read screenshot.", e);
        }
    }
}
