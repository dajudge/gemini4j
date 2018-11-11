package org.gemini4j.cucumber.reporter;

public class EventVisitor {
    public boolean visitLooksDifferentEvent(final LooksDifferentEvent e) {
        return false;
    }

    public boolean visitLooksSameEvent(final LooksSameEvent e) {
        return false;
    }

    public boolean visitNextTestEvent(final NextTestEvent e) {
        return false;
    }

    public boolean visitScreenshotNotKnownEvent(final ScreenshotNotKnownEvent e) {
        return false;
    }
}
