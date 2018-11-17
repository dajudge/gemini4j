package org.gemini4j.testapp.reporter;

import static org.junit.Assert.fail;

public class EventVisitor {
    public void visitLooksDifferentEvent(final LooksDifferentEvent e) {
        fail("Unexpected event: " + e);
    }

    public void visitLooksSameEvent(final LooksSameEvent e) {
        fail("Unexpected event: " + e);
    }

    public void visitNextTestEvent(final NextTestEvent e) {
        fail("Unexpected event: " + e);
    }

    public void visitScreenshotNotKnownEvent(final ScreenshotNotKnownEvent e) {
        fail("Unexpected event: " + e);
    }
}
