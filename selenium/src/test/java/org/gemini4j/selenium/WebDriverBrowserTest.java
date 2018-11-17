package org.gemini4j.selenium;

import com.palantir.docker.compose.DockerComposeRule;
import org.gemini4j.browser.Browser;
import org.gemini4j.core.Gemini4jConfiguration;
import org.gemini4j.diesel.Gemini4j;
import org.gemini4j.diesel.SuiteBuilder;
import org.gemini4j.imageresolver.ClasspathReferenceImageResolver;
import org.gemini4j.imageresolver.ReferenceImageResolver;
import org.gemini4j.reporter.Reporter;
import org.gemini4j.testapp.reporter.EventVisitor;
import org.gemini4j.testapp.reporter.LooksSameEvent;
import org.gemini4j.testapp.reporter.NextTestEvent;
import org.gemini4j.testapp.reporter.RecordingReporter;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;

import static org.gemini4j.testapp.util.TestAppUtil.testAppEnvironment;
import static org.gemini4j.testapp.util.TestAppUtil.uploadStaticResources;
import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.By.className;

public class WebDriverBrowserTest {
    @ClassRule
    public static DockerComposeRule DOCKER = testAppEnvironment();
    private RecordingReporter reporter = new RecordingReporter();

    @BeforeClass
    public static void setup() throws IOException {
        uploadStaticResources("/static");
    }

    @Test
    public void takes_screenshots() {
        suiteBuilder("takes_screenshots")
                .url(nginx("page1.html"))
                .snap("a")
                .build().run();
    }

    @Test
    public void records_actions() {
        takes_screenshots();
        assertStartedTestWithName("takes_screenshots");
        assertLookedTheSame("a");
        reporter.assertNoMoreEvents();
    }

    private void assertLookedTheSame(final String screenshotName) {
        reporter.assertVisitor(new EventVisitor() {
            @Override
            public void visitLooksSameEvent(final LooksSameEvent e) {
                assertEquals(screenshotName, e.getScreenshotName());
            }
        });
    }

    private void assertStartedTestWithName(final String suiteName) {
        reporter.assertVisitor(new EventVisitor() {
            @Override
            public void visitNextTestEvent(final NextTestEvent e) {
                assertEquals(suiteName, e.getTestName());
            }
        });
    }

    @Test
    public void clicks_buttons() {
        suiteBuilder("clicks_buttons")
                .url(nginx("app1.html"))
                .act(b -> b.delegate().findElement(className("clickMe")).click())
                .snap("1")
                .build().run();
    }

    @Test
    public void waits_for_conditions() {
        suiteBuilder("waits_for_conditions")
                .url(nginx("app1.html"))
                .snap("1")
                .build().run();
    }

    private String nginx(final String page) {
        return "http://nginx/static/" + page;
    }

    private SuiteBuilder<WebDriver> suiteBuilder(final String suiteName) {
        return Gemini4j.suite(suiteName, new Gemini4jConfiguration<WebDriver>() {
            @Override
            public Reporter getReporter() {
                return reporter;
            }

            @Override
            public ReferenceImageResolver getReferenceImageResolver() {
                return new ClasspathReferenceImageResolver("shots/" + suiteName);
            }

            @Override
            public Browser<WebDriver> getBrowser() {
                return new WebDriverBrowser("http://localhost:4444/wd/hub", new ChromeOptions());
            }
        });
    }
}
