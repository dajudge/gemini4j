package org.gemini4j.cucumber.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.gemini4j.api.Browser;
import org.gemini4j.cucumber.Gemini4jConfiguration;
import org.gemini4j.cucumber.Gemini4jPlugin;
import org.gemini4j.plugins.BrowserFactory;
import org.gemini4j.selenium.RemoteWebDriverBrowserFactory;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class BrowserSteps {
    public static final RemoteWebDriverBrowserFactory BROWSER_FACTORY = new RemoteWebDriverBrowserFactory(
            "http://localhost:4444/wd/hub",
            new ChromeOptions()
    );

    @Given("I open (.*)")
    public void navigateTo(String url) throws MalformedURLException {
        browser().navigateTo(new URL("http://nginx/" + url));
    }

    @When("^I click on the (.*) nav item$")
    public void clickNavItem(String navItem) {
        navItem(navItem).click();
    }

    @NotNull
    private WebElement navItem(final String navItem) {
        return webdriver().findElements(By.className("nav-link")).stream()
                .filter(it -> it.getText().equals(navItem))
                .findAny()
                .orElseThrow(() -> new AssertionError("Failed to find nav item '" + navItem + "'"));
    }

    private WebDriver webdriver() {
        return browser().delegate();
    }

    private Browser<WebDriver> browser() {
        return Gemini4jPlugin.getBrowser(new Gemini4jConfiguration<WebDriver>() {
            @Override
            public BrowserFactory<WebDriver> getBrowserFactory() {
                return BROWSER_FACTORY;
            }
        });
    }
}
