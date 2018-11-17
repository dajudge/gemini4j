package org.gemini4j.diesel;

import org.gemini4j.browser.Browser;
import org.gemini4j.core.Gemini4jContext;
import org.gemini4j.core.Snapper;
import org.gemini4j.utils.Clock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SuiteBuilderTest {
    private static final String SUITE_NAME = "someSuite";
    private SuiteBuilder<TestableBrowser> builder;

    private final long WAIT_FOR_TIMEOUT = 42;

    @Mock
    private TestableBrowser browser;
    @Mock
    private Gemini4jContext<TestableBrowser> context;
    @Mock
    private Clock clock;
    @Mock
    private Snapper<TestableBrowser> snapper;

    @Before
    public void setup() {
        when(context.getSnapper()).thenReturn(snapper);
        when(context.getBrowser()).thenReturn(browser);
        builder = new CommandSuiteBuilder<>(SUITE_NAME, clock, context, WAIT_FOR_TIMEOUT);
    }

    @Test
    public void snaps() {
        builder.snap("id").build().run();

        verify(snapper).snap("id");
    }

    @Test
    public void reports_to_reporter() {
        builder.snap("snap").build().run();

        verify(snapper).nextTest(SUITE_NAME);
    }

    @Test
    public void closes_context_properly() {
        builder.build().run();

        verify(context).shutdown();
    }

    @Test
    public void sets_url() throws MalformedURLException {
        final URL url = new URL("http://example.com");

        builder.url(url).build().run();

        verify(browser).navigateTo(url);
    }

    @Test
    public void provides_browser_access() {
        final TestableBrowser delegate = Mockito.mock(TestableBrowser.class);
        when(browser.delegate()).thenReturn(delegate);

        builder.act(b -> b.delegate().customBrowserMethod()).build().run();

        verify(delegate).customBrowserMethod();
    }

    @Test
    public void waits_for_condition() {
        final AtomicInteger counter = new AtomicInteger(0);
        builder.waitFor(browser -> counter.getAndIncrement() == 1).build().run();
    }

    @Test
    public void fails_when_condition_does_not_come_true() {
        doAnswer(call -> {
            when(clock.now()).thenReturn(WAIT_FOR_TIMEOUT + 1);
            return 0;
        }).when(clock).waitFor(anyLong());
        try {
            builder.waitFor(browser -> false).build().run();
            fail("Expected error");
        } catch (AssertionError e) {
            // expected
        }
    }

    interface TestableBrowser extends Browser {
        void customBrowserMethod();
    }
}
