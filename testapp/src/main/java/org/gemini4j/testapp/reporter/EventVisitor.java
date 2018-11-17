package org.gemini4j.testapp.reporter;

import static org.junit.Assert.fail;

public class EventVisitor {
    public void visitLooksDifferentEvent(final LooksDifferentEvent e) {
        nope(e);
    }

    public void visitLooksSameEvent(final LooksSameEvent e) {
        nope(e);
    }

    public void visitNextTestEvent(final NextTestEvent e) {
        nope(e);
    }

    public void visitScreenshotNotKnownEvent(final ScreenshotNotKnownEvent e) {
        nope(e);
    }

    private void nope(final RecordedEvent e) {
        fail("Unexpected event: " + e);
    }
}
