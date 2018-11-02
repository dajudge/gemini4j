package org.gemini4j.core;

import org.gemini4j.api.Browser;
import org.gemini4j.utils.Clock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("Suite builder")
@ExtendWith(MockitoExtension.class)
class SuiteBuilderTest {
    private SuiteBuilder<TestableBrowser> builder;

    private final long WAIT_FOR_TIMEOUT = 42;

    @Mock
    private Browser<TestableBrowser> browser;
    @Mock
    private ScreenshotProcessor screenshotProcessor;
    @Mock
    private Clock clock;

    @BeforeEach
    void setup() {
        builder = new CommandSuiteBuilder<>(clock, () -> browser, screenshotProcessor, WAIT_FOR_TIMEOUT);
    }

    @Test
    @DisplayName("closes browser interface after test run")
    void closes_browser_properly() {
        builder.build().run();

        verify(browser).shutdown();
    }

    @Test
    @DisplayName("sets the URL on the browser")
    void sets_url() throws MalformedURLException {
        final URL url = new URL("http://example.com");

        builder.url(url).build().run();

        verify(browser).navigateTo(url);
    }

    @Test
    @DisplayName("allows access to browser for interaction")
    void provides_browser_access() {
        final TestableBrowser delegate = mock(TestableBrowser.class);
        when(browser.delegate()).thenReturn(delegate);

        builder.act(b -> b.delegate().customBrowserMethod()).build().run();

        verify(delegate).customBrowserMethod();
    }

    @Test
    @DisplayName("waits for condition to come true")
    void waits_for_condition() {
        final AtomicInteger counter = new AtomicInteger(0);
        builder.waitFor(browser -> counter.getAndIncrement() == 1).build().run();
    }

    @Test
    @DisplayName("fails when condition does not come true")
    void fails_when_condition_does_not_come_true() {
        assertThrows(AssertionError.class, () -> {
            doAnswer(call -> {
                when(clock.now()).thenReturn(WAIT_FOR_TIMEOUT + 1);
                return 0;
            }).when(clock).waitFor(anyLong());
            builder.waitFor(browser -> false).build().run();
        });
    }

    interface TestableBrowser extends Browser {
        void customBrowserMethod();
    }
}
