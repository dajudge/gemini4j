package org.gemini4j.selenium;

import com.palantir.docker.compose.DockerComposeRule;
import org.gemini4j.diesel.Gemini4j;
import org.gemini4j.diesel.ScreenshotProcessor;
import org.gemini4j.diesel.SuiteBuilder;
import org.gemini4j.simile.Simile;
import org.jetbrains.annotations.NotNull;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.gemini4j.simile.Simile.newSimile;
import static org.gemini4j.testapp.TestAppUtil.testAppEnvironment;
import static org.gemini4j.testapp.TestAppUtil.uploadStaticResources;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.className;

public class SeleniumBrowserTest {
    private static final RemoteWebDriverBrowserFactory BROWSER_FACTORY = new RemoteWebDriverBrowserFactory(
            "http://localhost:4444/wd/hub",
            new ChromeOptions()
    );

    @ClassRule
    public static DockerComposeRule DOCKER = testAppEnvironment();

    @BeforeClass
    public static void setup() throws IOException {
        uploadStaticResources("/static");
    }

    @Test
    public void takes_screenshots() {
        suiteBuilder("takes_screenshots")
                .url(nginx("page1.html"))
                .snap("1")
                .build().run();
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

    @NotNull
    private String nginx(final String page) {
        return "http://nginx/static/" + page;
    }

    private static BufferedImage png(final String name) {
        final ClassLoader cl = SeleniumBrowserTest.class.getClassLoader();
        try (final InputStream stream = cl.getResourceAsStream(name)) {
            if (stream == null) {
                throw new IOException("Could not find reference screenshot " + name);
            }
            return ImageIO.read(stream);
        } catch (final IOException e) {
            throw new RuntimeException("Failed to load reference screenshot " + name);
        }
    }

    @NotNull
    private SuiteBuilder<WebDriver> suiteBuilder(final String suiteName) {
        return Gemini4j.suite(BROWSER_FACTORY, mustMatch(suiteName));
    }

    @NotNull
    private static ScreenshotProcessor mustMatch(final String suiteName) {
        return (id, shot) -> {
            final Simile simile = newSimile(shot, png(pathTo(suiteName, id))).build();
            final boolean result = simile.lookSame();
            if (!result) {
                try {
                    final File diffFile = new File("build/diff_" + suiteName + "_" + id + ".png");
                    final File shotFile = new File("build/shot_" + suiteName + "_" + id + ".png");
                    ImageIO.write(simile.diff(), "png", diffFile);
                    ImageIO.write(shot, "png", shotFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            assertTrue(result);
        };
    }

    private static String pathTo(final String suiteName, final String id) {
        return "shots/" + suiteName + "/" + id + ".png";
    }
}
