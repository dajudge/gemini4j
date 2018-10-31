package org.gemini4j.cucumber.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.gemini4j.core.Browser;
import org.gemini4j.cucumber.Gemini4jPlugin;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.net.URL;

public class BrowserSteps {


    @Given("I open (.*)")
    public void navigateTo(String url) throws MalformedURLException {
        browser().navigateTo(new URL("http://nginx/" + url));
    }

    @When("^I click on the (.*) nav item$")
    public void clickNavItem(String navItem) throws Throwable {
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
        return Gemini4jPlugin.getBrowser(WebDriver.class);
    }
}
